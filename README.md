### Nume student: Romulescu Florin-Sorin
### Grupa: 323CA

# Proiect: POO - TV.

## Etapa 1.

### Cuprins:
    1.Descrirere problemă
    2.Structură
    3.Implementare
        3.1.FileSystem
        3.2.FSActions
        3.3.Main
        3.4.Alte clase
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
            ContainInput.java
            CredentialsInput.java
            FilterInput.java
            Input.java
            MovieInput.java
            OutputFactory.java
                MoviesOutput
                OutputFactory
                OutTest
                StandardOutput
                UserLoggedInOutput
            SortInput.java
            UserInput.java
        filesystem/ -> pachet cu clasele care acționează asupra sistemului de fișiere
            FileSystem.java
            FSActions.java
            FSConstants.java
            Page.java


### **3.Implementare**


#### *3.1. FileSystem*
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

#### *3.2. FSActions*
În clasa `FSActions` avem metode statice pentru fiecare acțiune pe care un utilizator
o poate executa. Acestea vor întoarce o valoare `Boolean` în funcție de succesul
execuției.

Exemplu:
```Java
    public static Boolean changePage(final ActionInput action) {...}
```

#### *3.3 Main*

Funcția `mainLoop` iterează prin fiecare acțiune și creează un output specific.
Pentru determinarea metodei din clasa `FSActions` pe care vrem să o folosim, se
folosește un switch case. După switch case și executarea metodei, se va determina
un output specific, în funcție de rezultatul operației, pagina pe care ne aflăm și
operația executată. La final, în funcție de variabila locală `display` se scrie
în output.

#### *3.4 Alte clase*
Clasa `FSConstants` a fost folosită pentru stocarea constantelor. \
Clasa `Page` ține informații despre o pagină din sistemul de fișiere. \
Toate clasele din pachetul `fileio` (cu excepția lui `OutputFactory`) sunt folosite
pentru stocarea inputului.

### **4.Design Patterns**
Am folosit un design singleton asupra clasei `FileSystem` pentru a nu se pierde
referința către sistemul de fișiere. Când se trece la un nou test aceasta este setată
la `null` pentru a fi recreată. \
De asemenea, am folosit un design factory asupra clasei `OutputFactory`, deoarece
erau necesare mai multe tipuri de outputuri pe parcursul rulării, iar un design 
factory face implementarea și adăugarea de alte outputuri mult mai ușoară.

### **5.Ce am învățat din tema aceasta?**
* Să analizez o problemă pentru a vedea ce clase, designuri ar putea fi implementate
* Să implementez diferite design patternuri
* Să îmi structurez mai bine pachetele astfel încât să îmi fie mai ușor să navighez prin sursă
* Să scriu java docuri care mă pot ajuta în identificarea funcționalității unei funcții (am uitat de mai multe ori ce făcusem acum câteva zile ((= )
* Codul din `mainloop` arată ca o farfurie de spaghete, se putea lucra mai mult acolo