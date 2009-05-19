/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service.hibernate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.rough_diamond.commons.extractor.And;
import jp.rough_diamond.commons.extractor.Avg;
import jp.rough_diamond.commons.extractor.CombineCondition;
import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Count;
import jp.rough_diamond.commons.extractor.Desc;
import jp.rough_diamond.commons.extractor.Eq;
import jp.rough_diamond.commons.extractor.ExtractValue;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.FreeFormat;
import jp.rough_diamond.commons.extractor.Ge;
import jp.rough_diamond.commons.extractor.Gt;
import jp.rough_diamond.commons.extractor.In;
import jp.rough_diamond.commons.extractor.InnerJoin;
import jp.rough_diamond.commons.extractor.IsNotNull;
import jp.rough_diamond.commons.extractor.IsNull;
import jp.rough_diamond.commons.extractor.Join;
import jp.rough_diamond.commons.extractor.LabelHoldingCondition;
import jp.rough_diamond.commons.extractor.Le;
import jp.rough_diamond.commons.extractor.Like;
import jp.rough_diamond.commons.extractor.Lt;
import jp.rough_diamond.commons.extractor.Max;
import jp.rough_diamond.commons.extractor.Min;
import jp.rough_diamond.commons.extractor.NotEq;
import jp.rough_diamond.commons.extractor.NotIn;
import jp.rough_diamond.commons.extractor.Or;
import jp.rough_diamond.commons.extractor.Order;
import jp.rough_diamond.commons.extractor.Property;
import jp.rough_diamond.commons.extractor.RegularExp;
import jp.rough_diamond.commons.extractor.Sum;
import jp.rough_diamond.commons.extractor.SummaryFunction;
import jp.rough_diamond.commons.extractor.Value;
import jp.rough_diamond.commons.extractor.ValueHoldingCondition;
import jp.rough_diamond.framework.transaction.hibernate.HibernateUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

/**
 * ExtractorオブジェクトからHibernateのHQLを生成する
 * 本クラスは、Service層以外での動作保障はしない 
 */
@SuppressWarnings("unchecked")
public class Extractor2HQL {
    private final static Log log = LogFactory.getLog(Extractor2HQL.class);

    private final Extractor extractor;
    private StringBuilder builder;
    private Map<Class, ConditionStrategy> combineStrategy;
    private int patemeterIndex;
    private boolean usingGroupBy = false;
    
    private Extractor2HQL(Extractor extractor) {
        this.extractor = extractor;
        builder = new StringBuilder();
        combineStrategy = new HashMap<Class, ConditionStrategy>();
        combineStrategy.put(And.class, new AndStrategy());
        combineStrategy.put(Or.class, new OrStrategy());
        combineStrategy.put(Join.class, new JoinStrategy());
        patemeterIndex = 0;
    }
    
    /**
     * ExtractorオブジェクトからHibernateHQLを生成する
     * @param extractor 抽出条件格納オブジェクト
     * @return              HibernateのHQL
     */
    public static Query extractor2Query(Extractor extractor, LockMode lockMode) {
        Extractor2HQL tmp = new Extractor2HQL(extractor);
        return tmp.makeQuery(lockMode);
    }

	public static Query extractor2CountQuery(Extractor extractor) {
        Extractor2HQL tmp = new Extractor2HQL(extractor);
        return tmp.makeCountQuery();
	}

	/**
     * 抽出値からMapのリストを生成
     * @param list
     * @return
     */
    public static List<Map<String, Object>> makeMap(Extractor extractor, List<Object> list) {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        List<ExtractValue> values = extractor.getValues();
        MakeMapStrategy strategy = (values.size() == 1) ? SingleStrategy.INSTANCE : MultiStrategy.INSTANCE;
        for(Object row : list) {
        	Map<String, Object> rowMap = strategy.getMap(row, values);
            ret.add(rowMap);
        }
        return ret;
    }
    
    private static interface MakeMapStrategy {
    	public Map<String, Object> getMap(Object target, List<ExtractValue> values);
    }
    
    private static class SingleStrategy implements MakeMapStrategy {
    	private final static MakeMapStrategy INSTANCE = new SingleStrategy();
		public Map<String, Object> getMap(Object target, List<ExtractValue> values) {
			Map<String, Object> ret = new HashMap<String, Object>();
			Object value = ((values.get(0).value instanceof SummaryFunction) && target == null) ? 0L : target; 
			ret.put(values.get(0).key, value);
			return ret;
		}
    	
    }

    private static class MultiStrategy implements MakeMapStrategy {
    	private final static MakeMapStrategy INSTANCE = new MultiStrategy();
		public Map<String, Object> getMap(Object target, List<ExtractValue> values) {
			Object[] row = (Object[])target;
			Map<String, Object> rowMap = new HashMap<String, Object>();
			for(int i = 0 ; i < values.size() ; i++) {
				Object value = ((values.get(i).value instanceof SummaryFunction) && row[i] == null) ? 0L : row[i]; 
				rowMap.put(values.get(i).key, value);
			}
			return rowMap;
		}
    	
    }
    
	private Query makeCountQuery() {
        builder.append("select ");
        if(extractor.getValues().size() == 0) {
        	builder.append("count(distinct ");
        	builder.append(getAlias(extractor.target, extractor.targetAlias));
        	ClassMetadata cm = HibernateUtils.getSession().getSessionFactory().getClassMetadata(extractor.target);
        	builder.append(".");
        	builder.append(cm.getIdentifierPropertyName());
        	builder.append(")");
        } else {
        	builder.append("count(*)");
        }
        makeFromCouse(false);
        makeWhereCouse();
        String hql = builder.toString();
        log.debug(hql);
        Query query = HibernateUtils.getSession().createQuery(hql);
        setParameter(query);
        return query;
	}

    private Query makeQuery(LockMode lockMode) {
        makeSelectCouse(lockMode);
        makeFromCouse(true);
        makeWhereCouse();
        makeGroupByCouse();
        makeHavingCouse();
        makeOrderByCouse();
        String hql = builder.toString();
        log.debug(hql);
        Query query = HibernateUtils.getSession().createQuery(hql);
        setParameter(query);
        int offset = extractor.getOffset();
        if(offset > 0) {
            query.setFirstResult(offset);
        }
        int limit = extractor.getLimit();
        if(limit > 0) {
            query.setMaxResults(limit);
        }
        if(extractor.getValues().size() == 0) {
        	query.setLockMode(getAlias(extractor.target, extractor.targetAlias), lockMode);
        }
        if(extractor.getFetchSize() != Extractor.DEFAULT_FETCH_SIZE) {
        	query.setFetchSize(extractor.getFetchSize());
        }
        return query;
    }
    
	private void makeHavingCouse() {
		if(extractor.getHavingIterator().size() == 0) {
			return;
		}
        String joinString = "";
        builder.append(" having ");
        for(Condition<? extends SummaryFunction> con : extractor.getHavingIterator()) {
            builder.append(joinString);
            makeCondition(con);
            joinString = " and ";
        }
	}

	private void makeGroupByCouse() {
		if(!usingGroupBy) {
			return;
		}
		String delimitor = " group by ";
		for(ExtractValue ev : extractor.getValues()) {
			if(ev.value instanceof Property) {
				Property prop = (Property)ev.value;
				builder.append(delimitor);
				builder.append(getProperty(
						(prop.target == null) ? extractor.target : prop.target, prop.aliase, prop.property));
				delimitor = ", ";
			}
		}
	}

	private void makeOrderByCouse() {
        if(extractor.getOrderIterator().size() == 0) {
            return;
        }
        builder.append(" order by ");
        String delimitor = "";
        for(Order order : extractor.getOrderIterator()) {
            builder.append(delimitor);
            Class target;
            String targetAliase;
            if(order.target == null) {
                target = extractor.target;
                targetAliase = extractor.targetAlias;
            } else {
                target = order.target;
                targetAliase = order.aliase;
            }
            String property = getProperty(target, targetAliase, order.propertyName);
            builder.append(property);
            if(order instanceof Desc) {
                builder.append(" desc");
            }
            delimitor = ",";
        }
    }

    private void setParameter(Query query) {
        for(Condition<Property> con : extractor.getConditionIterator()) {
            setParameter(query, con);
        }
        for(Condition<? extends SummaryFunction> con : extractor.getHavingIterator()) {
            setParameter(query, con);
        }
    }

    private void setParameter(Query query, Condition<? extends Value> con) {
        if(con instanceof CombineCondition) {
            CombineCondition<? extends Value> cc = (CombineCondition<? extends Value>)con;
            for(Condition<? extends Value> c : cc.getConditions()) {
                setParameter(query, c);
            }
        } else if(con instanceof ValueHoldingCondition) {
            ValueHoldingCondition<? extends Value> vhc = (ValueHoldingCondition<? extends Value>)con;
            if(con instanceof In || con instanceof NotIn) {
            	Collection col = (Collection)vhc.value;
            	for(Object o : col) {
    	            query.setParameter(patemeterIndex, o);
    	            patemeterIndex++;
            	}
            } else {
	            query.setParameter(patemeterIndex, vhc.value);
	            patemeterIndex++;
            }
        }
    }
    
    private void makeWhereCouse() {
        if(extractor.getConditionIterator().size() + extractor.getInnerJoins().size() == 0) {
            return;
        }
        String joinString = "";
        builder.append(" where ");
        for(Condition<Property> con : extractor.getConditionIterator()) {
            builder.append(joinString);
            makeCondition(con);
            joinString = " and ";
        }
/*
        Set<String> set = new HashSet<String>();
        set.add(getAlias(extractor.target, extractor.targetAlias));
        //deletedinDBを強制的にチェック
        for(InnerJoin join : extractor.getInnerJoins()) {
            if(LogicalDeleteEntity.class.isAssignableFrom(join.target)) {
                String aliase = getAlias(join.target, join.targetAlias);
                if(!set.contains(aliase)) {
                    set.add(aliase);
                    builder.append(joinString);
                    builder.append(aliase);
                    builder.append(".deletedInDB");
                    builder.append("=");
                    builder.append("'");
                    builder.append(HibernateUtils.BOOLEAN_CHAR_F);
                    builder.append("'");
                    joinString = " and ";
                }
            }
            if(LogicalDeleteEntity.class.isAssignableFrom(join.joined)) {
                String aliase = getAlias(join.joined, join.joinedAlias);
                if(!set.contains(aliase)) {
                    set.add(aliase);
                    builder.append(joinString);
                    builder.append(aliase);
                    builder.append(".deletedInDB");
                    builder.append("=");
                    builder.append("'");
                    builder.append(HibernateUtils.BOOLEAN_CHAR_F);
                    builder.append("'");
                    joinString = " and ";
                }
            }
        }
*/
        for(InnerJoin join : extractor.getInnerJoins()) {
            builder.append(joinString);
            builder.append(getAlias(join.target, join.targetAlias));
            addProperty(join.targetProperty);
            builder.append("=");
            builder.append(getAlias(join.joined, join.joinedAlias));
            addProperty(join.joinedProperty);
            joinString = " and ";
        }
    }

    private void makeCondition(Condition<? extends Value> con) {
        if(con instanceof CombineCondition) {
            ConditionStrategy cs = combineStrategy.get(con.getClass());
            cs.makeWhereCouse((CombineCondition<? extends Value>)con);
        } else {
            LabelHoldingCondition<? extends Value> lhc = (LabelHoldingCondition<? extends Value>)con;
            String property = VALUE_MAKE_STRATEGY_MAP.get(lhc.label.getClass()).makeValue(extractor.target, lhc.label);
//            if(lhc.label instanceof Property) {
//            	property = makeLabel((Property)lhc.label);
//            } else {
//            	property = makeLabel((SummaryFunction)lhc.label);
//            }
            String where;
            if(lhc.getClass() == RegularExp.class) {
            	where = makeRegularExtWhere((RegularExp)lhc, property);
            } else if(lhc.getClass() == In.class) {
            	where = makeInWhere((In)con, property);
            } else if(lhc.getClass() == NotIn.class) {
            	where = makeNotInWhere((NotIn)con, property);
            } else {
            	where = MessageFormat.format(
                    CONDITION_TEMPLATE.get(lhc.getClass()), property);
            }
            builder.append(where);
        }
    }

//	private String makeLabel(SummaryFunction label) {
//		return VALUE_MAKE_STRATEGY_MAP.get(label.getClass()).makeValue(extractor.target, label);
//	}
//
//	private String makeLabel(Property prop) {
//        Class target;
//        String targetAliase;
//        if(prop.target == null) {
//            target = extractor.target;
//            targetAliase = extractor.targetAlias;
//        } else {
//            target = prop.target;
//            targetAliase = prop.aliase;
//        }
//        return getProperty(target, targetAliase, prop.property);
//    }
    
    private String makeRegularExtWhere(RegularExp lhc, String property) {
    	Dialect dialect = HibernateUtils.getDialect();
    	if(dialect == null) {
    		throw new RuntimeException("dialect can't get.");
    	}
    	String tmpl = REGEXP_TEMPLATE.get(dialect.getClass());
    	System.out.println(tmpl);
    	if(tmpl == null) {
    		throw new RuntimeException("unsupported regular Expression in " + dialect.getClass().getName() + ".");
    	}
    	return MessageFormat.format(tmpl, property);
	}

    private String makeNotInWhere(NotIn con, String property) {
    	return makeInWhare(con, property, "not in");
    }

    private String makeInWhere(In con, String property) {
    	return makeInWhare(con, property, "in");
    }
    
    private String makeInWhare(ValueHoldingCondition con, String property, String prefix) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(property);
    	sb.append(" ");
    	sb.append(prefix);
    	sb.append(" (");
    	String delimiter = "";
    	for(@SuppressWarnings("unused") Object o : (Collection)con.value) {
    		sb.append(delimiter);
    		sb.append("?");
    		delimiter = ", ";
    	}
    	sb.append(")");
    	return sb.toString();
    }
    
    private void addProperty(String targetProperty) {
        if(targetProperty == null || "".equals(targetProperty)) {
            return;
        }
        builder.append(".");
        builder.append(targetProperty);
    }
    
    private void makeSelectCouse(LockMode lockMode) {
        builder.append("select ");
        if(extractor.getValues().size() == 0) {
        	if(lockMode == LockMode.NONE) {
        		builder.append("distinct ");
        	}
            builder.append(getAlias(extractor.target, extractor.targetAlias));
            for(Order order : extractor.getOrderIterator()) {
                if(order.target == null) {
                	continue;
                }
                builder.append(",");
                Class target = order.target;
                String targetAliase = order.aliase;
                String property = getProperty(target, targetAliase, order.propertyName);
                builder.append(property);
            }
            return;
        }
        String delimitor = "";
        for(ExtractValue v : extractor.getValues()) {
            builder.append(delimitor);
            String property = VALUE_MAKE_STRATEGY_MAP.get(v.value.getClass()).makeValue(extractor.target, v.value);
            builder.append(property);
            delimitor = ",";
            if(v.value instanceof SummaryFunction) {
            	usingGroupBy = true;
            }
        }
    }

    static interface ValueMaker<T extends Value> {
    	public String makeValue(Class targetCl, T v);
    }
    final static Map<Class<? extends Value>, ValueMaker> VALUE_MAKE_STRATEGY_MAP;
    static {
    	Map<Class<? extends Value>, ValueMaker> tmp = new HashMap<Class<? extends Value>, ValueMaker>();
    	tmp.put(Property.class, new ValueMaker<Property>() {
			@Override
			public String makeValue(Class targetCl, Property v) {
				return getProperty(((v.target == null) ? targetCl : v.target), v.aliase, v.property);
			}
    	});
    	tmp.put(Max.class, new ValueMaker<Max>() {
			@Override
			public String makeValue(Class targetCl, Max v) {
				return "max(" + VALUE_MAKE_STRATEGY_MAP.get(v.value.getClass()).makeValue(targetCl, v.value) + ")";
			}
    	});
    	tmp.put(Min.class, new ValueMaker<Min>() {
			@Override
			public String makeValue(Class targetCl, Min v) {
				return "min(" + VALUE_MAKE_STRATEGY_MAP.get(v.value.getClass()).makeValue(targetCl, v.value) + ")";
			}
    	});
    	tmp.put(Sum.class, new ValueMaker<Sum>() {
			@Override
			public String makeValue(Class targetCl, Sum v) {
				return "Sum(" + VALUE_MAKE_STRATEGY_MAP.get(v.value.getClass()).makeValue(targetCl, v.value) + ")";
			}
    	});
    	tmp.put(Avg.class, new ValueMaker<Avg>() {
			@Override
			public String makeValue(Class targetCl, Avg v) {
				return "avg(" + VALUE_MAKE_STRATEGY_MAP.get(v.value.getClass()).makeValue(targetCl, v.value) + ")";
			}
    	});
    	tmp.put(Count.class, new ValueMaker<Count>() {
			@Override
			public String makeValue(Class targetCl, Count v) {
				return "count(" + 
						(v.distinct ? "distinct " : "") +
						VALUE_MAKE_STRATEGY_MAP.get(v.value.getClass()).makeValue(targetCl, v.value) + ")";
			}
    	});
    	tmp.put(FreeFormat.class, new ValueMaker<FreeFormat>() {
			@Override
			public String makeValue(Class targetCl, FreeFormat v) {
				int replaceIndex = 0;
				int length = v.format.length();
				StringBuilder ret = new StringBuilder();
				for(int i = 0 ; i < length ; i++) {
					char ch = v.format.charAt(i);
					if(ch == '?') {
						ret.append(replacetext(targetCl, v.values.get(replaceIndex++)));
					} else {
						ret.append(ch);
					}
				}
				return ret.toString();
			}

			String replacetext(Class targetCl, Object val) {
				if(val instanceof Value) {
					return VALUE_MAKE_STRATEGY_MAP.get(val.getClass()).makeValue(targetCl, (Value)val);
				} else if(val instanceof Number) {
					return val.toString();
				} else {
					return "'" + val.toString() + "'";
				}
			}
    	});
    	VALUE_MAKE_STRATEGY_MAP = Collections.unmodifiableMap(tmp);
    }
    
    
//    private Set<Class> joinedEntity;
    private void makeFromCouse(boolean isFetch) {
        Set<String> aliases = new HashSet<String>();
        builder.append(" from ");
        builder.append(getAlias(extractor.target, null));
        builder.append(" as ");
        builder.append(getAlias(extractor.target, extractor.targetAlias));
        if(extractor.getValues().size() == 0) {
            //参照オブジェクトのジョイン
//            joinedEntity = new HashSet<Class>();
            makeOuterJoin(extractor.target, getAlias(extractor.target, extractor.targetAlias), isFetch);
        }
        aliases.add(getAlias(extractor.target, extractor.targetAlias));
/*
        for(InnerJoin join : extractor.getInnerJoins()) {
            if(!joinedEntity.contains(join.target)) {
                joinedEntity.set(join.target);
            }
        }
*/
        for(InnerJoin join : extractor.getInnerJoins()) {
            String aliase = getAlias(join.target, join.targetAlias);
            if(!aliases.contains(aliase)) {
                aliases.add(aliase);
                builder.append(", ");
                builder.append(getAlias(join.target, null));
                builder.append(" as ");
                builder.append(aliase);
            }
            aliase = getAlias(join.joined, join.joinedAlias);
            if(!aliases.contains(aliase)) {
                aliases.add(aliase);
                builder.append(", ");
                builder.append(getAlias(join.joined, null));
                builder.append(" as ");
                builder.append(aliase);
            }
        }
    }
    
    private void makeOuterJoin(Class target, String currentAliase, boolean isFetch) {
//        joinedEntity.add(target);
        Map<String, Class> map = getEntityRelationProperty(target);
        for(Map.Entry<String, Class> entry : map.entrySet()) {
            builder.append(" left outer join ");
            if(isFetch) {
            	builder.append("fetch ");
            }
            String property = currentAliase + "." + entry.getKey();
            builder.append(property);
            Class cl = entry.getValue();
            if(!cl.equals(target)) {    //再帰を避ける
                makeOuterJoin(entry.getValue(), property, isFetch);
            }
        }
    }

    private Map<String, Class> getEntityRelationProperty(Class target) {
        ClassMetadata cm = HibernateUtils.getSession().getSessionFactory().getClassMetadata(target);
        Map<String, Class> ret = new HashMap<String, Class>();
        String[] names = cm.getPropertyNames();
        for(String name : names) {
            Type t = cm.getPropertyType(name);
            if(t.isEntityType()) {
                try {
                    ret.put(name, Class.forName(t.getName()));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ret;
    }
    
    private static String getProperty(Class cl, String aliase, String property) {
        aliase = getAlias(cl, aliase);
        if(property == null || "".equals(property)) {
            return aliase;
        } else if(property.equals("*")){
        	return "*";
        } else {
            return aliase + "." + property;
        }
    }
    
    private static String getAlias(Class cl, String alias) {
        if(alias == null || "".equals(alias)) {
            String name = cl.getName();
            return name.substring(name.lastIndexOf(".") + 1);
        } else {
            return alias;
        }
    }
    
    private static Map<Class , String> CONDITION_TEMPLATE;
    
    static {
        Map<Class, String> tmp = new HashMap<Class, String>();
        tmp.put(Eq.class, "{0} = ?");
        tmp.put(Ge.class, "{0} >= ?");
        tmp.put(Gt.class, "{0} > ?");
//展開する        
//        tmp.put(In.class, "{0} in (?)");
//        tmp.put(NotIn.class, "not ({0} in (?))");
        tmp.put(IsNotNull.class, "{0} is not null");
        tmp.put(IsNull.class, "{0} is null");
        tmp.put(Le.class, "{0} <= ?");
        tmp.put(Like.class, "{0} like ?");
        tmp.put(Lt.class, "{0} < ?");
        tmp.put(NotEq.class, "{0} <> ?");
//Dialect毎に実装を切り替える        
//        tmp.put(RegularExp.class, "char_length(substring({0},?)) > 0");
        CONDITION_TEMPLATE = Collections.unmodifiableMap(tmp);
    }

    private static Map<Class<? extends Dialect>, String> REGEXP_TEMPLATE;
    static {
        Map<Class<? extends Dialect>, String> tmp = new HashMap<Class<? extends Dialect>, String>();
        tmp.put(PostgreSQLDialect.class, "char_length(substring({0},?)) > 0");
        tmp.put(Oracle10gDialect.class, "REGEXP_INSTR({0}, ?) > 0");
        REGEXP_TEMPLATE = Collections.unmodifiableMap(tmp);
    }
    
    private interface ConditionStrategy {
        public void makeWhereCouse(CombineCondition<? extends Value> con);
    }

    class OrStrategy implements ConditionStrategy {
        public void makeWhereCouse(CombineCondition<? extends Value> con) {
            log.debug("OrStrategyを実行します。");
            if(con.getConditions().size() == 0) {
                return;
            }
            builder.append("(");
            String combineString = "";
            for(Condition<? extends Value> c : con.getConditions()) {
                builder.append(combineString);
                makeCondition(c);
                combineString = " or ";
            }
            builder.append(")");
        }

    }

    class AndStrategy implements ConditionStrategy {
        public void makeWhereCouse(CombineCondition<? extends Value> con) {
            if(con.getConditions().size() == 0) {
                return;
            }
            builder.append("(");
            String combineString = "";
            for(Condition<? extends Value> c : con.getConditions()) {
                builder.append(combineString);
                makeCondition(c);
                combineString = " and ";
            }
            builder.append(")");
        }
    }

    static class JoinStrategy implements ConditionStrategy {
        public void makeWhereCouse(CombineCondition con) {
            throw new RuntimeException("HQLではサポートしていません。（今のところ）");
        }
    }
}
