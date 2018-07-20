package webproject;

import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Сессионный CDI бин для взаимодействия с JSF страницами и передачи данных в EJB класс
@Named
@SessionScoped
public class WpCDI implements Serializable {

    @EJB
    //Инъекция EJB в CDI бин, чтобы передавать данные из JSF страницы через CDI бин в EJB класс.
    private WpEJB wpEJB;

    private String name;
    private String faculty;

    private String username;
    private String password;

    //Поле для вывода текущей информации на экран
    private String printInfo = "Информация о студенте";
    //Поле для сохранения списка выбранных студентов для удаления из БД
    private ArrayList<WpEntity> selectedStudents;

    public ArrayList<WpEntity> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(ArrayList<WpEntity> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrintInfo() {
        return printInfo;
    }

    public void setPrintInfo(String printInfo) {
        this.printInfo = printInfo;
    }

    //Метод для добавления студента в таблицу(БД)
    public void createStudent() {
        printInfo = wpEJB.createStudent(name, faculty);
    }

    //Метод удаления студента(ов) из таблицы(БД)
    public void removeStudentFromList() {
        printInfo = wpEJB.removeStudentFromList(selectedStudents);
    }

    //Метод для отображения списка студентов из БД в таблицу веб интерфейса сервиса. Возвращает список сущностей. Список сущностей берется из EJB
    public List<WpEntity> getAllStudents() {
        return wpEJB.getAllStudents();
    }

    //Метод для проверки вводимых данных на странице авторизации(логин и пароль)
    public String loginControl(){
        if (wpEJB.loginControl(username, password)){
            return "create.xhtml?faces-redirect=true";
        }
        RequestContext.getCurrentInstance().update("growl");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User or Password invalid!!!"));
        return "";
    }
}
