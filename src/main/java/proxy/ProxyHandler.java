package proxy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

//import org.apache.log4j.Logger;

/**
 * 处理代理地址的SERVLET
 * 
 * (修改说明): 请求串默认用javascript的encodeURIComponent()进行转码操作,
 * 故在本SERVELT中将不再进行转码操作,统一使用URLDecoder.decode进行解码 字符集默认设置为UTF-8
 */
public class ProxyHandler extends HttpServlet {

	// 序列值
	private static final long serialVersionUID = 1L;

	// 日志对象
	//private static Logger logger = Logger.getLogger(ProxyHandler.class);

	// 默认字符集常量 : UTF-8
	private static final String CHARSET_UTF8 = "UTF-8";
	
	public static final String CONTENT_URL_NAME = "url";

	/**
	 * SERVELT初始化
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
	}

	/**
	 * doGet请求
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		noSecurityRequest(req, resp);
	}

	/**
	 * 处理doGet请求的实现
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void noSecurityRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		
		//logger.info("ProxyHandler noSecurityRequest 请求类型: GET");
		String url0 = "";
		OutputStream out = null;
		HttpURLConnection conn = null;
		// 获取请求的类型与字符集
		String requestContent = req.getContentType();
		String requestCharset = req.getCharacterEncoding();
		StringBuffer requestContentType = new StringBuffer();
		if (null != requestContent) {
			requestContentType.append(requestContent);
			if (null != requestCharset) {
				requestContentType.append(";charset=").append(requestCharset);
			}
		}

		try {
			// 获取请求参数的字符串
			url0 = req.getQueryString();
			// 进行解码操作
			if (null != url0)
				url0 = URLDecoder.decode(url0, CHARSET_UTF8);
			if (url0.startsWith("url=")) {
				url0 = url0.substring(4);
			}
			if (url0.indexOf("requestTime") != -1) {
				url0 = url0.split("requestTime")[0];
				url0 = url0.substring(0, url0.length() - 1);
			}
		//	System.out.println("contentURL:"+url0);
			// 请求转发
			URL url = new URL(url0);
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			//如果requestContentType为空字符串，不设置Content-Type属性
			if(!requestContentType
					.toString().equals("") ){
				conn.setRequestProperty("Content-Type", requestContentType
						.toString());
			}
			

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			// 获取请求的类型与字符集设置
			String contentType = conn.getContentType();
			String encoding = conn.getContentEncoding();

			out = resp.getOutputStream();
			// 设置返回的类型与字符集
			resp.setContentType(contentType);
			//resp.setCharacterEncoding(encoding);//weblogic 下不支持此属性，所以屏蔽掉
			
			
			/*旧的代理处理返回输出代码  2012-09-11*/
			/**BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), CHARSET_UTF8));
			BufferedWriter wd = new BufferedWriter(new OutputStreamWriter(resp
					.getOutputStream(), CHARSET_UTF8));

			String tempLine = rd.readLine();
			while (tempLine != null) {
				wd.write(tempLine);
				logger.info(tempLine);
				tempLine = rd.readLine();
			}
			wd.flush();*/
			
			/*新的代理处理返回输出代码,针对处理flex的地图显示  2012-09-11*/
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			out=resp.getOutputStream();
			int len = 0;
			byte[] b = new byte[1024];
			while((len=in.read(b)) >0){
				out.write(b,0,len); 
			}
			out.flush();
			/*新的代理处理返回输出代码,针对处理flex的地图显示 2012-09-11*/
			
			//logger.info("\n请求地址: " + url0 + "\n请求成功!");
		} catch (Exception e) {
			//logger.error("请求地址: " + url0 + "\n请求失败: " + e);
		//	resp.sendError(500);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
				//logger.error(e);
			}
		}
	}

	/**
	 * doPost请求
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//logger.info("请求类型: POST");
		// Document indoc = null;
		// try {
		// // 解析客户端发送的请求
		// SAXReader builder = new SAXReader();
		// indoc = builder.read(new InputStreamReader(
		// request.getInputStream(), "UTF-8"));
		// logger.info("请求信息：\n" + indoc.asXML());
		// } catch (Exception ex) {
		// logger.error("解析客户端发送的请求错误!\n" + ex);
		// }
		InputStream indoc = request.getInputStream();
		// 获取请求参数
		String url0 = request.getQueryString();
		// 对请求参数进行解码操作
		if (null != url0)
			url0 = URLDecoder.decode(url0, CHARSET_UTF8);

		// 获取请求类型与字符集
		String requestContent = request.getContentType();
		String requestCharset = request.getCharacterEncoding();
		StringBuffer requestContentType = new StringBuffer();
		if (null != requestContent) {
			requestContentType.append(requestContent);
			if (null != requestCharset) {
				requestContentType.append(";charset=").append(requestCharset);
			}
		}

		OutputStream out = response.getOutputStream();
		try {
			if (url0.startsWith("url=")) {
				String urlString = url0.substring(4);

				URL url = new URL(urlString);

				BufferedInputStream in = null;
				HttpURLConnection connection = null;

				byte[] bs = null;
				if (url != null) {
					try {
						// 转发请求
						connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("POST");
						connection.setRequestProperty("Content-Type",
								requestContentType.toString());
						connection.setDoInput(true);
						connection.setDoOutput(true);

						OutputStream toserver = connection.getOutputStream();
						// OutputFormat format = OutputFormat
						// .createCompactFormat();
						// format.setEncoding(CHARSET_UTF8);
						// XMLWriter writer = new XMLWriter(toserver, format);
						// writer.write(indoc);
						int l = 0;
						while ((l = indoc.read()) != -1) {
							toserver.write(l);
						}
						toserver.flush();
						toserver.close();

						// 获取转发返回的类型与字符集
						String responseContentType = connection
								.getContentType();
						String responseCharset = connection
								.getContentEncoding();

						// 将转发返回的类型与字符集设置到返回的参数中
						response.setContentType(responseContentType);
						response.setCharacterEncoding(responseCharset);

						// 读取响应信息，并将响应发送到客户端
						in = new BufferedInputStream(connection
								.getInputStream());
						bs = new byte[1024];
						int startpos = 0;
						int num = 0;
						num = in.read(bs, startpos, 1024);
						//logger.info("返回信息:");
						while (num != -1) {
							out.write(bs, 0, num);
							//logger.info(new String(bs));
							num = in.read(bs, 0, 1024);
						}
						//logger.info("\n请求地址: " + url0 + "\n请求成功!");
					} catch (IOException e) {
						//logger.error("请求地址: " + url0 + "\n请求失败!" + e);
					//	response.sendError(500);
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (Exception ex) {
							}
						}
						if (connection != null) {
							try {
								connection.disconnect();
							} catch (Exception ex) {
							}
						}
					}
				}
			}
		} catch (Exception e) {
			//logger.error(e);
			//   response.sendError(500);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
}
