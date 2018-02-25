package webproject;

import javax.persistence.Entity;
import javax.persistence.Id;

//Класс сущность, используется для отображения в БД

@Entity
public class WpEntity {

    @Id
    //Поле для ввода/сохранения ФИО студента
    private String name;
    //Поле для ввода/сохранения факультета студента
    private String faculty;

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
}
