package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {
    public static void main(String[] args) throws Exception {
        String projectName = "Masterjava";

        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = parser.unmarshal(Resources.getResource("payload.xml").openStream());

        //getting groups included in the project
        Project project = (Project) payload.getProjects().getProject().stream()
                .filter(p -> projectName.equals(p.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("wrong project name"));

        List<Group> groupsOfProject = project.getGroups().getGroup().stream()
                .map(jaxbElement -> (Group) jaxbElement.getValue())
                .collect(Collectors.toList());

        //find user that included in project groups
        List<User> projectUsers = payload.getUsers().getUser().stream()
                .filter(user -> {
                    List<Group> userGroups = user.getGroups().getGroup().stream()
                            .map(g -> (Group) g.getValue())
                            .collect(Collectors.toList());
                    for (int i = 0; i < groupsOfProject.size(); i++) {
                        if (userGroups.contains(groupsOfProject.get(i))) {
                            return true;
                        }
                    }
                    return false;
                })
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());

        projectUsers.forEach(user -> System.out.println(user.getFullName()));
    }
}
