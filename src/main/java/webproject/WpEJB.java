package webproject;

import org.apache.commons.lang3.StringUtils;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

//EJB класс. В нем отрабатывается вся бизнесс-логика - отображение списка, добавление и удаление в БД. Получает данные из JSF страниц через CDI бин.

@Stateless
public class WpEJB {

    //Менеджер сущностей для работы с классом WpEntity, отображающимся на БД
    @PersistenceContext(unitName = "wpPU")
    private EntityManager entityManager;

    //Поле для сохранения и вывода на экран текущей информации
    private String printInfo;

    //Метод для добавления записи(студента) в таблицу(БД)
    public String createStudent(String name, String faculty) {

        //Проверяем пустоту заполняемых полей
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(faculty)) {
            printInfo = "Вы не ввели данные";
            return printInfo;
        }

        //Если поля заполнены то пытаемся по переданному полю name найти сущность в БД
        WpEntity wpEntity = entityManager.find(WpEntity.class, name);

        //Если сущность найдена и поле faculty найденной сущности равно переданному значению поля faculty, то нам нельзя создавать студента с таким же ФИО
        if (wpEntity != null && faculty.equals(wpEntity.getFaculty())) {
            printInfo = name + " уже добавлен в БД";
            return printInfo;
        }

        //Если сущность не найдена, то такую сущность создаем, устанавливаем ей необходимые поля и с помощью метода persist сохраняем эту сущность в БД
        wpEntity = new WpEntity();
        wpEntity.setName(name);
        wpEntity.setFaculty(faculty);
        entityManager.persist(wpEntity);
        printInfo = name + " добавлен(а) в БД";

        return printInfo;
    }

    //Метод собирающий все записи в БД и отображающий их в таблицу веб интерфейса сервиса
    public List<WpEntity> getAllStudents() {
        //С помощью метода createQuery создается объект Query - запрос(язык hql)
        Query query = entityManager.createQuery("select entity from WpEntity entity");
        //Функция getResultList возвращает список сущностей, которые соответствуют сущности WpEntity
        return query.getResultList();
    }

    //Метод удаления записей из БД. Использует список selectStudents для сохранения списка выбранных полей
    public String removeStudentFromList(ArrayList<WpEntity> selectedStudents) {

        //Поле метода для сохранения текущего ФИО из списка возвращаемых записей
        String name = null;

        //С помощью цикла пробегаемся по списку выбранных записей
        for (int i = 0; i < selectedStudents.size(); i++) {
            //Сохраняем в поле метода значение поля name(ФИО) из каждой выбранной записи
            name = selectedStudents.get(i).getName();
            //По полю name из каждой записи находим сущность в БД
            WpEntity wpEntity = entityManager.find(WpEntity.class, selectedStudents.get(i).getName());
            //С помощью метода remove удаляем найденную сущность из таблицы(БД)
            entityManager.remove(wpEntity);
        }

        //Проверяем перед нажатием кнопки "Удалить" выбрана ли запись для удаления
        if (selectedStudents.size() == 0) {
            return printInfo = "Вы не указали, удаляемую строку";
        }
        //Если выбрана одна запись, то выводим информацию для одной записи, если выбрано больше одной записи, то выводим информацию для всех записей
        if (selectedStudents.size() == 1) {
            return printInfo = name + " удален(а) из БД";
        } else return printInfo = "Студенты удалены из БД";
    }
}
