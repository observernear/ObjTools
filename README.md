# OBJ Reader/Writer

Я написал собственный модуль [ObjWriter.java](src/com/cgvsu/objwriter/ObjWriter.java), предназначенный для сохранения модели в формат Obj.
Подготовил [юнит-тесты](tests/com/cgvsu/objwriter/ObjWriterTest.java) для проверки моего модуля.
Проект реализует полный цикл работы с 3D моделями: загрузку, обработку и сохранение.

## Возможности

- **Чтение OBJ файлов** - полная поддержка формата .obj
- **Запись OBJ файлов** - сохранение моделей в стандартном формате
- **Валидация данных** - проверка корректности моделей
- **Сравнение файлов** - утилита для проверки идентичности моделей
- **Поддержка всех компонентов**:
    - Вершины (v)
    - Текстурные координаты (vt)
    - Нормали (vn)
    - Полигоны/грани (f)

## Основные модули

- ### ObjWriter
  - ```java
    public class ObjWriter {
        public static void write(Model model, String filePath)
        public static String modelToString(Model model)
        public static String modelToString(Model model, String comment)
    }
    ``` 
  - Методы:
    - **write(Model, String)** - сохраняет модель в файл
    - **modelToString(Model)** - возвращает строковое представление модели
    - **modelToString(Model, String)** - с пользовательским комментарием

- ### FileCompareObj
  - ```java
    public class FileCompareObj implements FileCompareInt {
        public void compareFiles()
        public void printDifferenceSummary()
        public boolean areFilesIdentical()
        public void compareFilesContent()
    }
    ``` 
  - Методы:
      - **compareFiles()** - сравнивает два OBJ файла и выводит статистику по элементам
      - **printDifferenceSummary()** - показывает сводку различий между файлами
      - **areFilesIdentical()** - проверяет, идентичны ли файлы побайтово
      - **compareFilesContent()** - выполняет детальное сравнение содержимого файлов


## Пример работы
```Java
// Загрузка модели
Model model = ObjReader.read(Files.readString(Path.of("model.obj")));

// Просмотр статистики
System.out.println("Vertices: " + model.vertices.size());
System.out.println("Textures: " + model.textureVertices.size());
System.out.println("Polygons: " + model.polygons.size());

// Сохранение модели
ObjWriter.write(model, "model_output.obj");

// Проверка идентичности
FileCompareObj comparator = new FileCompareObj(
    Path.of("model.obj"), 
    Path.of("model_output.obj")
);
comparator.compareFiles();
```

## Формат поддерживаемых данных
- Вершины
```text
v x y z
v 1.0 0.0 0.0
```
- Текстурные координаты
```text
vt u v
vt 0.5 0.5
```
- Нормали
```text
vn x y z
vn 0.0 1.0 0.0
```
- Полигоны
```text
f v1 v2 v3                          # Только вершины
f v1/vt1 v2/vt2 v3/vt3              # Вершины + текстуры
f v1//vn1 v2//vn2 v3//vn3           # Вершины + нормали
f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3  # Все компоненты
```

## Тестирование
Проект включает комплексные модульные тесты, проверяющие:
- Корректность форматирования чисел
- Обработку различных комбинаций данных (только вершины, с текстурами, с нормалями)
- Обработку ошибок (null модели, NaN значения)
- Корректность индексации (0-based → 1-based)
- Сохранение структуры данных

## Обработка ошибок
- [ObjReaderException.java](src/com/cgvsu/objreader/ObjReaderException.java) - ошибки чтения
- [ObjWriterException.java](src/com/cgvsu/objwriter/ObjWriterException.java) - ошибки записи