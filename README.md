# ShareBoi applikasjon - Gruppe 6

Vi er 6 studenter på NTNU som våren 2018 har gjennomført et prosjekt i emnet TDT4140 - Programvareutvikling.
Prosjektet gikk ut på å lage en applikasjon for en produkteier. Applikasjonen skulle tilfredsstille produkteieren sine krav til brukerhistorier, 
men vi stod fritt til å velge tekniske løsninger selv.
Vi har benyttet utviklingsmetoden *Scrum*, som du kan lese mer om [her](file:///C:/Users/emiln/Documents/Skole/4.%20semester/Programvareutvikling/Pensum/Scrum-PU.pdf).
Arbeidskravene til emnet som prosjektet har fulgt finnes [her](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/wikis/Arbeidskrav-i-TDT4140-v%C3%A5ren-2019).

Applikasjonen vi skulle utvikle for profukteieren var en delingsapp for forbruksvarer, for eksempel tyggis, paracet, snus, eller liknende.
Som bruker av appen skulle man kunne sende forespørsler til andre brukere om produkter, godta/avslå forespørsler og se posisjon til andre brukere.
For full oversikt over alle brukerhistorier som er implementert i appen, se [userstories i wikien](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/wikis/Userstories).

Vi har benyttet oss av Java og JavaFX/FXML som programmeringsspråk og brukt MongoDB som database. Google Maps sine karttjenester har også blitt implementert.

## Hvordan bruke applikasjonen

Dette er en beskrivelse av hvordan du får en kopi av prosjektet på din egen pc, som du kan bruke til å kjøre applikasjonen eller viderutvikle den.

### Forutsetninger

Applikasjonen forutsetter at man har Eclipse, JDK 11 og JavaFX 11 installert. 
Om man mot formodning ikke har JDK11, kan JDK 9 også benyttes ved å lese manualen nedenfor samt [Endre JDK](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/wikis/Endre-versjon-i-Java)

### Installasjon

For å kunne kjøre og/eller videreutvikle appen må du ha Eclipse installert på din lokale maskin. Du kan installere Eclipse [her](https://wiki.eclipse.org/Eclipse_Installer).
Det er også mulig å benytte seg av andre program som for eksempel IntelliJ, men denne instruksjonsguiden tar utgangspunkt i bruk av Eclipse.

For å få opp prosjektet vårt i Eclipse må du klone repositorien vår fra GitLab. Det gjøres på følgende måte:
1. Åpne Eclipse
2. Velg **File** > **Import**  
3. Velg **Git** > **Projects from Git (smart import)** > **Next**  
4. Velg **Clone URI** > **Next**  

     ![final_1](/uploads/15b5c5f828693044caaa4fa518d4f47d/final_1.JPG)  
5. Kopier inn følgende i øverste feltet: `https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6`.    
   På *User* og *Password* benytt samme brukernavn og passord som du bruker for å logge inn med Feide. 
6. **Next** > **Next**
7. Velg **Browse** > **Lagre** > **Next** > **Finish**  

   ![final_2](/uploads/a1efcc76e9b9d2d6c38fbc21c68a69d3/final_2.JPG)  

Du har nå fått lastet ned en kopi av repositorien vår. For å sørge for at den er helt oppdatert, gjør følgende:
1. Høyreklikk på **gruppe-6 [master] - ...** > **Pull**. Du vil nå få opp et fetch result, trykk **close**.
2. Høyreklikk på **puApp [gruppe-6 master]** > **Maven** > **Update project** > **OK**  

   ![maven_1](/uploads/3bdc68dd9c4230f53fad8c8ac1aeb57c/maven_1.JPG)

Nå skal prosjektet forhåpentligvis være oppdatert og uten errors. Følgende innstillinger må i tillegg gjøres:
1. Velg **Run** > **Run Configuration**
2. Gå inn på fanen **Arguments** og legge inn følgende paths:  
   Mac: `--module-path /path/to/javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml`  
   Windows: `--module-path "\path\to\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml`  
  og  
  `--add-modules=javafx.web`  

   ![final_3](/uploads/4e7d4ea5901e5b9bec465f3dc5a7f564/final_3.JPG)

   


<!---For å få kart i applikasjonen må du laste ned kartet: [maps.html](/uploads/f4c2ea3dfb72dba33682c70ae756e0b2/maps.html) 
og legge det i følgende path lokalt på pc-en din:  
`C:\Users\"brukernavn"\git\gruppe-6\puApp\target\classes\com\lynden\gmapsfx`--->

Dersom du er på Mac og mot formodning ikke får kjørt applikasjonen, kan du følge veiledningen [Endre JDK](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/wikis/Endre-versjon-i-Java).

### API-nøkler

Veiledning for oppsett av API-nøkler finner man her: [API-veiledning](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/wikis/Oppsett-av-API-n%C3%B8kler)
API-nøkler ligger allerede inne, slik at disse kan benyttes. Disse API-nøklene er koblet opp mot en gratis brukerkonto, slik at man kan gjøre flere tusen kall før det ev. koster penger. 


### Kjøre applikasjonen

For å kjøre appen åpner du **mainPackage**, dobbeltklikk på **Main.java** og trykk deretter på den grønne knappen *Run Main*.
Får du fortsatt ikke kjørt appen, [sjekk ut denne linken](https://openjfx.io/openjfx-docs/) under Eclipse med Maven (Non-modular). 
For videre bruk av appen se [brukermanual](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/wikis/Brukermanual) i wikien.

Best opplevelse av appen får med følgende påloggingsinformasjon:
`username: foo@foo.com`
`password: Passord123`

For å få tilgang til admin-rettigheter benytt følgende påloggingsinformasjon:  
`username: admin`  
`password: passord`

### Testing

Test i applikasjonen ligger i klassen [UserTest](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-6/blob/master/puApp/src/core/userPackage/UserTest.java)

## Forfattere
- **Hanne Olssen** - *Scrum Master*
- **Stine Linn Skjerven** - *Utvikler*
- **Christian Vennerød** - *Utvikler*
- **Morten Dahl** - *Utvikler*
- **Borger Melsom** - *Utvikler*
- **Emil Neby** - *Utvikler*

### Bygget med:

[Maven](https://maven.apache.org/)   
[Java 11](https://docs.oracle.com/en/java/javase/11/)   
[JavaFX 11](https://openjfx.io/openjfx-docs/)      
[MongoDB Javadriver](https://mongodb.github.io/mongo-java-driver/)     
[MongoDB Atlas](https://www.mongodb.com/cloud/atlas)    
[GMapsFX](https://rterp.github.io/GMapsFX/)       

### Anerkjennelser:

Takk til vår produkteier Theodor for bra oppfølging, Babak for et bra opplegg og oss selv :- )
