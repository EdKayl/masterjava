package ru.javaops.masterjava.servlet;

import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet("/")
@MultipartConfig(location = "d:\\tmp")
public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Part file = req.getPart("data");
        List<User> users = null;
        try {
            users = parseUsingStaxJAXB(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(users);
        resp.sendRedirect("/upload");
    }

    private List<User> parseUsingStax(Part file) throws IOException {
        ArrayList<User> result = new ArrayList();
        try (StaxStreamProcessor processor = new StaxStreamProcessor(file.getInputStream())){
            while(processor.startElement("User", "Users")){
                User user = new User();
                user.setEmail(processor.getAttribute("email"));
                user.setFlag(FlagType.fromValue(processor.getAttribute("flag")));
                user.setValue(processor.getText());
                result.add(user);
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<User> parseUsingStaxJAXB(Part file) throws IOException, JAXBException {
        ArrayList<User> result = new ArrayList();
        try (StaxStreamProcessor processor = new StaxStreamProcessor(file.getInputStream())){
            while(processor.startElement("User", "Users")){
                JaxbParser parser = new JaxbParser(User.class);
                User user = parser.unmarshal(processor.getReader(), User.class);
                result.add(user);
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return result;
    }


}
