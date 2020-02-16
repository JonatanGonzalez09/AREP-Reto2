package edu.escuelaing.arep.reto2;

import java.io.*;
import java.net.*;
import java.util.List;

import edu.escuelaing.arep.reto2.impl.UsuarioImpl;

public class HttpServer {
	
	private static Socket clientSocket;
	private static ServerSocket servSocket;
	private String serverMessage;
	UsuarioImpl usuario = new UsuarioImpl();

	/**
	 * Class constructor. Set attributes values.
	 */
	public HttpServer() {
		int puerto = getPort();
		HttpServer.clientSocket = null;
		try {
			HttpServer.servSocket = new ServerSocket(puerto);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * This method starts the server
	 */
	public void startServer() {
		while (true) {
			try {
				System.out.println("Estableciendo la conexión...");
				HttpServer.clientSocket = HttpServer.servSocket.accept();
				System.out.println("Cliente establecido...");
				BufferedReader entrada = new BufferedReader(new InputStreamReader(HttpServer.clientSocket.getInputStream()));
				String path = this.getRequest(entrada);
				OutputStream os = clientSocket.getOutputStream();

				if ("/dataUser.html".equals(path)) {
					dataBase(os);
				} else {
					pageNotFound(os);
				}
				HttpServer.clientSocket.close();
			}

			catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}
	}

	public String getRequest(BufferedReader entrada) {
		boolean notExit = true;
		String path = null;
		try {
			while ((this.serverMessage = entrada.readLine()) != null && notExit) {

				if (this.serverMessage.contains("GET")) {
					String[] dir = this.serverMessage.split(" ");
					path = dir[1];
					notExit = false;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return path;
	}

	private void dataBase(OutputStream os) {
		PrintWriter res = new PrintWriter(os, true);
		String htmlPage = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>"
				+ "<html lang=\"es\">" + "<head>"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
				+ "    <title>Usuarios</title>"
				+ "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css\">"
				+ "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js\" itegrity=\"sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=\" crossorigin=\"anonymous\"></script>"
				+ "</head>" + "<body>" + "    <header>" + "        <div class=\"row\">"
				+ "            <div class =\"col l8 m8 s12 offset-l2 offset-m2 center\">"
				+ "                <h1>Usuarios registrados en la base de datos</h1>" + "            </div>"
				+ "        </div>" + "        <div class=\"row\">"
				+ "            <div class=\"col l8 m8 s12 offset-l2 offset-m2\">" + "                <table>"
				+ "                    <thead>"
				+ "                        <tr class=\"card-panel red accent-4 white-text\">"
				+ "                            <th class=\"center-align\">ID Usuario</th>"
				+ "                            <th class=\"center-align\">Nombre</th>"
				+ "                            <th class=\"center-align\">Correo</th>"
				+ "                            <th class=\"center-align\">Apellido</th>"
				+ "                        </tr>" + "                    </thead>" + "                    <tbody>"
				+ datosUsuarios() + "                    </tbody>" + "                </table>" + "            </div>"
				+ "        </div>" + "    </header>" + "</body>"
				+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js\"></script>"
				+ "</html>";

		setOutput(res, htmlPage, os);
	}

	private String datosUsuarios() {
		String usuarioInfo = "";
		List<Usuario> allUsuarios = usuario.getUsuarios();

		for (int i = 0; i < allUsuarios.size(); i++) {
			Usuario c = allUsuarios.get(i);
			usuarioInfo += "<tr>" + "<td class=\"center-align\">" + c.getId() + "</td>" + "<td class=\"center-align\">"
					+ c.getNombre() + "</td>" + "<td class=\"center-align\">" + c.getCorreo() + "</td>"
					+ "<td class=\"center-align\">" + c.getApellido() + "</td>" + "</tr>";
		}
		return usuarioInfo;
	}

	private void pageNotFound(OutputStream os) {

		PrintWriter res = new PrintWriter(os, true);
		String outputLine = "HTTP/1.1 404 \r\n\r\n<html><body><h1>Page Not Found</h1></body></html>";
		setOutput(res, outputLine, os);
	}

	private void setOutput(PrintWriter res, String outputLine, OutputStream os) {
		res.println(outputLine);
		res.flush();
		res.close();

		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static int getPort() {
		if (System.getenv("PORT") != null) {
			return new Integer(System.getenv("PORT"));
		}
		return 35000;
	}

	public static void main(String args[]) throws IOException {
		HttpServer server = new HttpServer();
		System.out.println("Iniciando servidor");
		server.startServer();
		servSocket.close();
	}
	
}