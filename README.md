# Proiect: POO - TV.

### Cuprins:
    1.Descrirere problemă
    2.Structură
    3.Implementare
        3.1.Database
        3.2.FileSystem
        3.3.Types
        3.4.Features
        3.5.OutputFactory
        3.6.Main
        3.7.Alte clase
    4.Design patterns
    5.Ce am învățat din tema aceasta?

### **1.Descriere problemă**
În acest proiect am avut în vedere realizarea unei platforme pentru vizualizarea de filme
și seriale, care să răspundă la anumite funcționalități precum `login`, `register` sau
`purchase`.

Inputul a fost salvat într-un obiect numit `Input` prin metoda `objectMapper.readValue()`.

```Java
class Input {
    private ArrayList<UserInput> users;
    private ArrayList<MovieInput> movies;
    private ArrayList<ActionInput> actions;
}
```

Outputul problemei a fost scris într-un obiect `ArrayNode` creat cu ajutorul metodei
`<OutputObject>.createObjectNode()`.

### **2.Structură**
    src/
        Main.java -> fișierul cu funcția main
        fileio/ -> pachet cu clasele de input și output
            ActionInput.java
            Input.java
            OutputFactory.java
                MoviesOutput
                OutputFactory
                OutTest
                StandardOutput
                UserLoggedInOutput
            SortInput.java
            [...]
        filesystem/ -> pachet cu clasele care acționează asupra sistemului de fișiere
            FileSystem.java
            FSConstants.java
            Page.java
        features/ -> pachet cu toate clasele care implementeaza un feature
            strategy/ -> pachet cu clasele care implementeaza un design pattern strategy
                FeatureBuilder
                FeatureContext
                FeatureStrategy
            DataBaseAddFeature
            DataBaseDeleteFeature
            [...]
        type/ -> pachet cu toate clasele care implementeaza un tip de feature
            strategy/ pachet cu clasele care implementeaza un design pattern strategy
                ActionBuilder
                ActionContext
                TypeStrategy
            ChagePageType
            DataBaseType
            [...]
        database/ -> pachet cu clasa DataBase plus 2 clase care ajuta la implementarea unui design pattern observer
            Database
            NotificationService
            UserListener


### **3.Implementare**

#### *3.1 Database*
Pentru a memora toate datele despre filme, utilizatori și starea outputului
am creat clasa `Database`, în care am utilizat un design pattern singleton, deoarece
ne dorim ca aceasta să nu fie reinițializată pe parcursul execuției. \
Când trecem la testul următor setăm instanța clasei cu null pentru a fi
reinițializată.

```Java
public final class Database {
    private NotificationService notificationService;
    private static Database instance = null;
    private final FileSystem fileSystem;
    private List<UserInput> users;
    private List<MovieInput> movies;
    private boolean display;
    private boolean moviesChangeable;

    private Database() {
        FileSystem.setInstanceNull();
        fileSystem = FileSystem.getInstance();
        users = new ArrayList<>();
        movies = new ArrayList<>();
        display = false;
        moviesChangeable = true;
    }
    /* ... */
}
```

Pentru a notifica utilizatorul atunci când se produc schimbări în baza de date
(se adaugă / șterge un film) am implementat un design pattern `Observer`.
Print metoda `newMovieNotifier` apelăm metoda din atributul `notificationService`
care apoi pentru fiecare `listener` va apela metoda `update`. Aici vom actualiza
notificările utilizatorului curent. \

Clasa `UserListener` reprezintă un tip de film la care cel puțin un utilizator
s-a abonat. Aceasta este creată la folosirea funcționalității `subscribe` și este
adăugată în lista `listeners` din atributul `notificationService`.


#### *3.2 FileSystem*
Pentru crearea sistemului de fișiere am creat o clasă `FileSystem` în care am folosit
un design pattern de tip `singleton` pentru a menține instanța clasei pe tot parcursul
testului.

```Java
public final class FileSystem {
    private static FileSystem instance = null;
    private final Page unAuthPage;
    private final Page authPage;
    private Page current;
    private UserInput currentUser = null;
    private List<MovieInput> currentMovies = null;
    private MovieInput currentMovie = null;
    private  List<MovieInput> allMovies = null;
    private boolean visited = false;

    public static FileSystem getInstance() {
        if (instance == null) {
            instance = new FileSystem();
        }
        return instance;
    }
    
    /*...*/
}
```
Tot prin această clasă vom ține minte utilizatorul curent, filmele pe care le poate vedea,
filmul pe care l-a accesat și toate filmele existente pe platformă.

#### 3.3 Types
Pentru a face mai lizibil codul, dar și pentru a face mai ușoară implementarea
de tipuri noi am folosit un design pattern `Strategy`. Astfel am creat o clasă
`ActionContext` care prin metoda `createStrategy()` îmi inițializează câmpul
`strategy` cu un obiect `TypeStrategy` care implementează metoda `action()`.
Această metodă va executa funcționalitatea corespunzătoare și îmi va întoarce
dacă operația a fost realizată cu succes (true/false). \
Deoarece clasa `ActionContext` are foarte multe câmpuri opționale, am creat
o clasă `ActionBuilder` care implementează un design pattern `Builder`.
Restul claselor vor implementa interfața `TypeStrategy` cu funcționalitatea
lor respectivă.

#### 3.4 Features

Similar cu ce am zis la subpunctul `3.3` vom folosi un design pattern `Strategy`
pentru implementarea claselor din pachetul `features`. \
- `ActionContext` implementează design patternul `strategy`.
- `ActionBuilder` construiește un obiect `ActionContext`.
- `FeatureStrategy` o interfață care conține o metodă action.
- Restul claselor implementează interfața cu funcționalitatea lor respectivă.

#### 3.5 OutputFactory
Deoarece avem mai multe tipuri de output am implementat un design pattern `Factory`
care îmi va genera un output specific în funcție de parametrul pe care îl primește.

```Java
public static ObjectNode createOutput(final OutputType type,
                                      final String error,
                                      final List<MovieInput> currentMovieList,
                                      final UserInput currentUser) {
    return switch (type) {
        case StandardOutput -> new StandardOutput(error).convertToObjectNode();
        case UserLoggedInOutput ->
                new UserLoggedInOutput(error, currentUser).convertToObjectNode();
        case MoviesOutput ->
                new MoviesOutput(error, currentMovieList, currentUser).convertToObjectNode();
        case RecommendationOutput ->
                new RecommendationOutput(currentUser).convertToObjectNode();
    };
}
```

Toate celelalte clase moștenesc o clasă abstractă `OutTest`.

#### *3.6 Main*

Funcția `mainLoop` iterează prin fiecare acțiune și creează un output specific.
Pentru determinarea acțiunii specifice vom crea un obiect de tip `ActionContext`,
vom crea o strategie și o vom apela. Codul arată așa:

```Java
ActionContext actionContext = new ActionBuilder()
                    .currentMovieList(currentMoviesList)
                    .movies(instance.getMovies())
                    .users(instance.getUsers())
                    .action(action)
                    .build();
actionContext.createStrategy();
boolean ret = actionContext.action();
```

După vom determina tipul de output printr-un control flow și vom adăuga în
obiectul `output` rezultatul dacă este necesar. La final vom genera recomandările
pentru utilizatorul premium.

#### *3.7 Alte clase*
- Clasa `FSConstants` a fost folosită pentru stocarea constantelor.
- Clasa `Page` ține informații despre o pagină din sistemul de fișiere.
- Toate clasele din pachetul `fileio` (cu excepția lui `OutputFactory`) sunt folosite
pentru stocarea inputului.
- Clasele din pachetul `types` sunt folosite pentru implementarea funcționalităților
primite ca input în atributul `action.type`
- Clasele din pachetul `features` sunt folosite pentru implementarea funcționalităților
primite ca input în atributul `action.feature`

### **4.Design Patterns**
- *Singleton* : L-am folosit în clasele `Database` și `FileSystem`
- *Factory* : L-am folosit în fișierul `OutputFactory.java`
- *Builder*: L-am folosit în clasele `ActionBuilder` și `FeatureBuilder`
- *Strategy*: L-am folosit în clasele din pachetele `features` și `types`
- *Observer*: L-am folosit în clasele din pachetul `database`

### **5. Ce am învățat din tema aceasta?**
- Să utilizez multiple design patternuri
- Să scriu cod ușor modificabil
- Să îmi structurez mai bin funcționalitățile
- Să analizez o problemă + să îmi creez un plan de implementare
