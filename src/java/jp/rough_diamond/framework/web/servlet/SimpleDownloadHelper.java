package jp.rough_diamond.framework.web.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author e-yamane
 * �_�E�����[�h�R���e���c�̐����Əo�͂𕪗����邽�߂̃w���p�[�N���X
 */
public class SimpleDownloadHelper {
	public void execute(HttpServletResponse response) throws IOException {
		response.setContentType(getContentType());
		String fileName = getFileName();
		if(fileName != null) {
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + fileName + "\"");
		}
		InputStream in = getContent ();
        try {
    		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
    		int n;
    		int count = 0;
    		while ((n = in.read()) >= 0) {
    			count++;
    			out.write(n);
    		}
    		response.setContentLength(count);
    		out.flush();
        } finally {
            in.close();
        }
	}

	private String fileName;
	private String contentType;
	private InputStream content;

	public InputStream getContent() {
		return content;
	}

	public String getContentType() {
		if(contentType == null) {
			return "application/octet-stream";
		} else {
			return contentType;
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setContent(InputStream stream) {
		content = stream;
	}

	public void setContentType(String string) {
		contentType = string;
	}

	public void setFileName(String string) {
		fileName = string;
	}
}