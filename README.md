# Specifiche dell'applicativo

## Specifiche

Si desidera realizzare, secondo un processo di sviluppo incrementale/iterativo,
un sistema software di supporto al baratto di articoli
(dotati di consistenza fisica) riconducibili a un insieme prefissato
(non vuoto) di categorie.

L’applicazione può essere adottata da varie organizzazioni, che sovrintendono
al baratto di categorie diverse di articoli e/o operano supiazze diverse.

L’applicazione prevede due tipologie di utente, il **configuratore** e il **fruitore**.
Il primo è un esponente dell’organizzazione che, attraverso l’applicazione
software, sovrintende agliscambi di beni senza uso di denaro;
egli è deputato alla descrizione delle categorie di articoli scambiabili e può
ottenere informazioni relative agli attuali baratti potenziali o a quelli già
avvenuti.

Il secondo tipo di utente è una persona che si rivolge all’applicazione per
prendere visione degli articoli pubblicati da altri (fruitori)
come barattabili ed eventualmente effettuare degli scambi.

Egli deve compilare dei campi atti a descrivere ciascun articolo che intende
barattare.

Dopo avere descritto almeno un articolo, il fruitore può
(ma non deve necessariamente) avanzare la proposta di barattare un suo articolo
conquello di un altro fruitore così come rispondere alla proposta di baratto
avanzata da un altro fruitore.

Si assume che le due parti coinvolte in un baratto si incontrino poi di persona
al fine di scambiarsi i due articoli oggetto del baratto stesso.

Si noti che un fruitore può anche non descrivere mai alcun articolo,
limitandosi a scorrere le descrizioni di articoli pubblicate dagli altri fruitori.

## Generalità

L’applicazione è tesa a supportare il baratto di articoli (dotati di consistenza fisica)
afferenti ad alcune categorie stabilite dal configuratore, a ciascuna delle quali corrisponde un insieme specifico di campi atti a descrivere ogni articolo (che si intende barattare) appartenente alla categoria stessa.

Ogni singola categoria è dotata di un `nome` e di una `descrizione`, entrambe stringhe in *linguaggio naturale* volte a esplicare il significato della categoria stessa.

> Per esempio, il nome di una categoria può essere `Libro` e la sua descrizione `Opera cartacea stampata`.

Una categoria può articolarsi in **due o più** (sotto)categorie, a loro volta suddividibili **ricorsivamente**, e così via, secondo una gerarchia ad albero.

> Ad esempio, la radice di una gerarchia può essere la categoria `Libro` ed essa può articolarsi nelle (sotto)categorie `Romanzo`, `Saggio`, `Opera a fumetti`, `Numero di un fumetto periodico` e `Testo scolastico`. A sua volta la categoria Romanzo può articolarsi nelle (sotto)categorie `Romanzo di letteratura italiana` e`Romanzo di letteratura straniera`, ecc.

Il nome di ciascuna categoria è **unico all’interno della gerarchia di appartenenza**.

*N.B. Non si deve erroneamente pensare che una gerarchia definisca ontologicamente un concetto (quello relativo alla radice), declinandolo in tutte le  sfumature possibili.*

La gerarchia comprende invece **solo le categorie che sono di interesse ai fini delle operazioni di baratto** che l’organizzazione che si è dotata dell’applicazione intende sostenere.

La medesima applicazione può considerare **più gerarchie**. Vige il vincolo **di unicità del nome di ciascuna categoria radice** entro la totalità delle categorie radice di tali gerarchie.

Gli articoli da scambiare devono appartenere **solo alle categorie foglia** delle gerarchie considerate.

A ciascuna categoria (a qualsiasi livello essa si collochi entro la gerarchia di appartenenza) compete inoltre un insieme (eventualmente vuoto) di **campi nativi**, la compilazione di ciascuno dei quali da parte del fruitore è obbligatoria o facoltativa.

Di seguito, la compilazione di un campo che non è esplicitamente indicata come obbligatoria è da intendersi come facoltativa.

Ogni categoria radice è dotata almeno dei seguenti due campi nativi:

- `Stato di conservazione` a compilazione **obbligatoria**. Impone al fruitore di
 comunicare lo stato di conservazione e usura in cui si trova l’articolo proposto in baratto
- `Descrizione libera` a compilazione **facoltativa** consente al fruitore di descrivere l’articolo proposto in baratto mediante un testo libero

**Ciascuna categoria figlio eredita poi la totalità dei campi (nativi o ereditati) della categoria padre e i campi nativi devono avere un nome distinto da quello dei campi ereditati.**

> Ad esempio, i campi nativi che competono alla categoria Libro (oltre ai due sopraindicati, comuni a tutte le categorie radice) potrebbero essere i seguenti:
>
>- `Titolo` a compilazione obbligatoria
>- `Autore/i` a compilazione obbligatoria
>- `Casa editrice`
>- `Anno di stampa`
>
> L’insieme dei campi nativi della categoria Romanzo potrebbe essere vuoto mentre quello della categoria Romanzo di letteratura straniera potrebbe essere dotato di un solo campo nativo:
>
>- `Lingua di stampa` a compilazione obbligatoria

Ogni articolo da barattare può essere descritto dal fruitore assegnando un valore a ciascuno dei campi (*sia nativi sia derivati*) che competono alla categoria **foglia** di appartenenza dello stesso.

**Ogni valore è inserito dal fruitore sotto forma di una stringa di caratteri.**

>Supponendo che un fruitore intenda proporre il baratto di un articolo appartenente alla
categoria (foglia) Romanzo di letteratura straniera, egli potrebbe, ad esempio, compilare i campi come segue:
>
>- Stato di conservazione: `nuovo`
>- Descrizione libera: `volume a copertina flessibile mai sfogliato (doppione ricevuto in regalo)`
>- Titolo: `A farewell to arms`
>- Autore/i: `Ernest Hemingway`
>- Casa editrice: ``
>- Anno di stampa: ``
>- Lingua di stampa: `Inglese`

## Versioni

### Versione 1

La prima versione dell’applicazione consente l’accesso del solo configuratore.

Ogni configuratore effettua il primo accesso sfruttando credenziali predefinite (comunicate a ciascun nuovo configuratore che intende registrarsi), che lo qualificano come appartenente al gruppo (non vuoto, eventualmente individuale) dei configuratori dell’applicazione.

Nell’ambito della sessione aperta al primo accesso egli dovrà immediatamente scegliere
credenziali personali, da usare in tutti gli accessi successivi: solo a valle di tale scelta egli potrà operare sul back-end dell’applicazione.

Lo username di ciascun configuratore lo individua univocamente.

La prima versione dell’applicazione consente al configuratore di

- introdurre una nuova gerarchia di categorie
- dotare ogni categoria di tale gerarchia di nome, descrizione e di tutti i suoi campi
nativi, indicando l’eventuale obbligatorietà di ciascuno
- salvare in modo persistente tutte le informazioni di cui ai punti precedenti
- visualizzare ciascuna gerarchia presente e tutte le informazioni a corredo della stessa.

#### Progresso

- [x] Creazione infrastruttura di base e database
- [x] Creazione account configuratore (tool esterno)
- [x] Modifica password utente
- [x] Creazione nuova gerarchia

### Versione 2

La seconda versione dell’applicazione consente l’accesso anche al fruitore.

Ogni **fruitore** deve scegliere al primo accesso le sue credenziali individuali, che gli consentiranno di operare sul front-end dell’applicazione.

Lo username di ciascun fruitore individua univocamente il **fruitore** stesso (esso non deve coincidere con lo username di alcun altro fruitore né di alcun configuratore).

La seconda versione dell’applicazione dà la facoltà al **configuratore** di fissare il valore dei seguenti parametri:

- `Piazza`: la città in cui avvengono gli scambi; per ipotesi, tale città è unica e, una
volta stabilita, non può più cambiare
- `Luoghi`: alcuni luoghi (*al limite uno solo*) in cui tali scambi sono effettuati
- `Giorni`: il giorno o i giorni della settimana in cui gli scambi possono avere luogo
- `Intervalli orari`: gli intervalli orari (*almeno uno*) entro cui effettuare gli scambi, dove i soli orari in corrispondenza dei quali si possono fissare appuntamenti finalizzati allo scambio di articoli fra le due parti coinvolte in un baratto sono quelli dello scoccare dell’ora e della mezz’ora;
- `Scadenza`: il numero massimo di giorni entro cui un fruitore può accettare una
proposta di scambio avanzata da un altro fruitore.

>Ad esempio, il configuratore può fissare i seguenti valori:
>
>- Piazza: `Brescia`
>- Luoghi: `Piazzale Kossuth, zona nord del parcheggio`
>- Giorni: `[giovedì, venerdì]`
>- Intervalli orari”: `[17.00-19.30]`
>
>Si noti che l’intervallo orario sopra esemplificato implica che gli appuntamenti possano essere fissati (solo) alle ore 17.00, 17.30, 18.00, 18.30, 19.00 e 19.30.

Infine, la seconda versione dell’applicazione visualizza, a beneficio del fruitore, il nome e la descrizione delle categorie radice di tutte le gerarchie, nonché la piazza, i luoghi, i giorni e gli intervalli orari in cui sono possibili gli scambi.

#### Progresso

- [x] Registrazione fruitore
- [x] Visualizzazione frontend
- [x] Impostazioni

### Versione 3

La terza versione consente al **fruitore** di pubblicare le informazioni circa un articolo che egli intende barattare.

A tal fine il **fruitore** deve individuare (*magari con l’aiuto dell’applicazione*) la categoria foglia di appartenenza e compilare i campi (nativi ed ereditati) relativi alla stessa.

L’applicazione accetta la pubblicazione relativa a un articolo solo se sono stati compilati almeno i campi obbligatori di pertinenza.

Le informazioni circa la categoria di appartenenza di tale articolo e il contenuto della pubblicazione accettata (valori dei campi), nonché il collegamento fra la stessa e lo username del fruitore autore di suddetta pubblicazione, vengono salvati in modo **persistente**.

Una pubblicazione accettata rappresenta un’offerta di baratto che si trova nello stato di `Offerta aperta`.

Il fruitore autore di una `Offerta aperta` non può modificare le informazioni fornite in merito all’articolo da barattare, egli può però ritirare tale offerta, trasformandone così lo stato in quello di `Offerta ritirata`.

L’applicazione deve mantenere traccia dei passaggi di stato subiti da un’offerta, salvando le informazioni relative a tali passaggi in modo **persistente**.
*Nota di teto: Questa è una blockchain LOL*

In ogni momento, il **configuratore** e così pure il **fruitore** può indicare una categoria (foglia) di una qualsiasi gerarchia e visualizzare tutte le attuali Offerte aperte relative a tale categoria.

Inoltre, il **fruitore** può visualizzare tutte le `Offerte aperte` e le `Offerte ritirate` di cui è autore, indipendentemente dalla categoria di appartenenza

#### Progresso

- [x] Creazione di offerte
- [x] Modifica degli stati dell'offerta
- [x] Visualizzazione offerte in gerarchia e ricerca
- [x] Visualizzazione offerte utente

### Versione 4

Questa versione consente a una coppia di fruitori di accordarsi per un baratto.

Il fruitore autore di una `Proposta aperta` (*relativa a un articolo A*) può infatti scegliere un’altra `Proposta aperta` (*relativa a un articolo B*) *solo se di questa non è autore ed essa appartiene alla stessa categoria foglia*.

>*Nota di teto: Non dovrebbero essere Offerte?*
>
>Il fruitore autore di una `Offerta aperta` (*relativa a un articolo A*) può infatti scegliere un’altra `Offerta aperta` (*relativa a un articolo B*) *solo se di questa non è autore ed essa appartiene alla stessa categoria foglia*.

In tal modo il fruitore autore della prima proposta indica la sua disponibilità ad accettare B in cambio di A.

In questo caso l’offerta relativa ad A passa nello stato di `Offerta accoppiata`, lo stato dell’offerta relativa a B cambia in quello di `Offerta selezionata` e viene creato un collegamento fra le due offerte.

Se il fruitore autore di una `Offerta selezionata` non risponde alla proposta del fruitore autore della corrispondente `Offerta accoppiata` entro il numero di giorni
prestabilito (*pari al valore del parametro di configurazione “Scadenza”*), allo scadere di tale termine entrambe le offerte ritornano allo stato di `Offerta aperta`.

Il fruitore che invece risponde (**necessariamente affermativamente**) alla proposta del fruitore autore dell’`Offerta accoppiata` corrispondente entro il numero di giorni prestabilito, nella risposta deve indicare gli estremi (luogo, data e ora) di un possibile appuntamento (in occasione del quale effettuare lo scambio dei due oggetti).

Nel momento in cui tale risposta è inviata, entrambe le offerte coinvolte passano allo  stato di `Offerta in scambio`, mantenendo sempre traccia l’una dell’altra.

Il fruitore autore della (ex) `Offerta accoppiata` deve ora rispondere, entro il numero
massimo di giorni (*il solito valore del parametro di configurazione “Scadenza”*),
accettando tale appuntamento o scartandolo e proponendone uno alternativo (in altro luogo e/o data e/o ora, fra quelli consentiti), e così via.

Lo scambio di proposte di appuntamenti prosegue fino a quando si verifica una di queste condizioni:

- una delle due parti accetta l’appuntamento proposto dall’altra,
- la parte che deve rispondere alla proposta di appuntamento non lo fa entro i termini stabiliti.

Se si verifica la prima condizione, l’una e l’altra `Offerta in scambio` passano nello
stato di `Offerta chiusa`.

Se invece si verifica la seconda condizione, l’una e l’altra `Offerta in scambio` recidono il collegamento reciproco e passano nello stato di `Offerta aperta`.

Ciascun fruitore può visualizzare tutte le offerte di cui è autore, **indipendentemente dallo stato e dalla categoria di appartenenza di ognuna**. Inoltre, per ciascuna Offerta in scambio di cui è autore, egli può visualizzare l’ultima risposta fornita dall’autore dell’offerta a essa collegata.

Si noti che, al passaggio di un’offerta dallo stato di `Offerta aperta` a quello di `Offerta selezionata` (e, analogamente, al passaggio di un’offerta dallo stato di `Offerta accoppiata` allo stato di `Offerta in scambio`), l’autore della prima si rende conto del fatto che è invitato a rispondere al fruitore autore della seconda e se ne rende conto proprio in virtù di tale passaggio (di cui può acquisire consapevolezza solo se accede all’applicazione e visualizza le offerte di sua pertinenza), **non già perché gli viene inviato un messaggio (ad esempio, di posta elettronica)** (Sad teto).

Parimenti, quando sopra si parla di “invio” di una risposta da parte di un fruitore, si intende che tale fruitore può “allegare” la risposta, cioè la proposta di un appuntamento, a un’`Offerta selezionata` oppure a un’`Offerta in scambio` di cui è autore e che suddetta risposta è visualizzabile dal fruitore autore dell’offerta collegata.

Quest’ultimo, però, si rende conto di quale appuntamento è stato proposto solo se accede all’applicazione e richiede la visualizzazione della risposta “allegata” all’offerta collegata.

Quindi, la comunicazione non avviene mai attraverso messaggi (ad esempio, di posta elettronica) inviati esplicitamente da un fruitore a un altro (i fruitori ignorano lo username degli altri fruitori e non hanno modo di contattarli direttamente).

Il **configuratore** può indicare una categoria foglia di una gerarchia e visualizzare le attuali Offerte in scambio nonché le Offerte chiuse relative ad articoli di tale categoria.

#### Progresso

- [ ] Sistema di proposte da visualizzazione Offerte aperte
- [ ] Stati `Offerta accoppiata`, `Offerta selezionata`, `Offerta in scambio`
- [ ] Interfacce di messaggistica tra utenti
- [ ] Gestione eventi temporizzati (script cron?)

### Versione 5

La quinta versione consente al configuratore di importare gli ingressi del back-end dell’applicazione, ovvero gerarchie delle categorie e valori dei parametri di configurazione, in modalità batch (cioè attraverso uno o più file di input) anziché in modalità interattiva.

#### Progresso

- [ ] File di configurazione

## Requisiti non Funzionali

- Il modello di processo da adottare è incrementale/iterativo.
- Il linguaggio di programmazione da utilizzare è Java.
- L’architettura esterna da realizzare per l’applicazione è stand alone.
- Requisito non prescrittivo ma importante in sede di valutazione è l’impiego di precondizioni, postcondizioni e invarianti di classe entro il codice Java.
- Non è richiesta la creazione di una interfaccia utente grafica (tuttavia è bene che
l’architettura interna sia progettata in modo da ridurre gli effetti collaterali e lo sforzo connesso al cambiamento se in futuro il sistema di interazione testuale fosse sostituito da una GUI).
- Non è richiesto l’impiego di alcun DBMS (Data Base Management System).

## Note

Ogni versione da produrre estende le funzionalità della precedente, **senza modificarle**.

Nei requisiti sopra enunciati si sono ignorati i possibili aspetti legali connessi alla gestione di un’applicazione volta a supportare il baratto di articoli, all’uso della stessa e al baratto vero e proprio.

È evidente che ogni applicazione che operi nel mondo reale deve rispettare la legislazione del Paese in cui viene utilizzata.

Tale legislazione potrebbe imporre, per esempio, che ciascun fruitore fornisca le sue generalità oppure che dichiari di essere maggiorenne, che autocertifichi che gli articoli proposti in baratto rientrano nella sua personale disponibilità e non sono frutto di attività illecite, ecc.

Inoltre, le Offerte aperte dovrebbero essere pubblicate solo se non contengono (ad esempio, entro la descrizione dell’articolo mediante un testo libero) messaggi inaccettabili (perché offensivi o altro).

Queste considerazioni mettono in luce l’opportunità di un ulteriore tipo di utente,
il **moderatore**, che dovrebbe effettuare controlli di questo genere (così come quelli volti a evitare truffe).

La ragione per cui questa ulteriore figura di utente e tali controlli – essenziali in una applicazione professionale – sono stati ignorati è che l’applicazione che
sarà realizzata è da intendere come il frutto di un esercizio, necessariamente di dimensioni limitate, e non è destinata a essere effettivamente installata e usata nella realtà quotidiana, come sottolineato dal requisito di adottare un’architettura esterna stand alone (mentre una applicazione che supporta il baratto nel mondo reale dovrebbe operare in rete).

Per la stessa ragione non si è mai parlato del ritorno atteso da parte dell’organizzazione che, attraverso l’applicazione, sovrintende alle operazioni di baratto.

Tale ritorno esula completamente dagli scopi didattici del progetto.

**Nelle pagine precedenti si è assunto che i valori dei campi atti a descrivere un articolo che un fruitore desidera scambiare siano solo stringhe di caratteri.
Nel caso più generale, alcuni valori potrebbero essere di tipo diverso, talvolta anche enumerativo (con richiesta al fruitore di fornire risposte chiuse).** *JSON SCHEMA?*

Inoltre, potrebbe essere contemplato che il fruitore carichi opzionalmente dei file (ad esempio, una fotografia dell’articolo che intende scambiare).

Quest’ultimo è uno scenario complesso, in cui il requisito di sicurezza gioca
un ruolo di rilievo.

Tale requisito, sempre più importante in un lavoro professionale, non
è stato considerato nei limiti dell’esercizio proposto né il gruppo di lavoro deve tenerne conto.
L’enfasi del progetto non è infatti sulla sicurezza, pertanto anche l’attribuzione di
credenziali a configuratore e fruitore non è ritenuta un’operazione critica.

**L’interpretazione relativa alle "credenziali predefinite" comunicate a ciascun nuovo
configuratore che intende registrarsi, di cui ai requisiti funzionali della prima versione, non è univoca.**

Secondo l’interpretazione più semplice – e meno sicura – tali credenziali
sono uniche e immutabili, stabilite al momento dell’installazione dell’applicazione.

**Un’interpretazione più complessa è quella secondo cui le credenziali predefinite sono diverse per ciascun configuratore che intende registrarsi.
In questo caso, al primo configuratore che intende registrarsi vengono comunicate credenziali predefinite stabilite al momento dell’installazione dell’applicazione mentre a ciascuno di quelli successivi vengono comunicate nuove credenziali, fissate di volte in volta da un configuratore già registrato.**

Questa soluzione è più sicura ma non ne è richiesta la realizzazione.

Secondo la trattazione precedente, il baratto è consentito solo fra articoli afferenti alla medesima categoria (foglia). In futuro si potrebbe consentire il baratto anche fra articoli di categorie compatibili, dove la compatibilità è stabilita a priori dal configuratore e salvata permanentemente.

Tutte le versioni dell’applicazione attualmente previste assumono che ciascuna gerarchia
di categorie, una volta introdotta dall’utente, sia immutabile (e pertanto è molto
importante che tale gerarchia sia stata congegnata con attenzione). In futuro si potrebbe permettere l’estensione di una gerarchia attraverso l’aggiunta di sottoalberi.

Se il padre della radice del sottoalbero da aggiungere è una categoria foglia, l’intervento sulla gerarchia comporta effetti collaterali sulle offerte non ancora chiuse né ritirate che cadono in tale categoria.

I requisiti non funzionali non impongono alcuna tecnologia da utilizzare per la memorizzazione persistente dei dati (categorie, valori di configurazione, offerte e relativi passaggi di stato) né per l’importazione (non interattiva) degli input del back-end.

Per quanto riguarda la prima, la scelta dei progettisti potrebbe cadere banalmente sulla
serializzazione di oggetti.

Per quanto riguarda la seconda, esistono più soluzioni, fra cui l’impiego di file di testo aventi una sintassi definita appositamente dai progettisti stessi (ad esempio, file in formato CSV o JSON o XML ...).
Si noti che tali file potrebbero essere adottati anche per il salvataggio.

I requisiti (funzionali e non) delle cinque versioni dell’applicazione da realizzare sono deliberatamente espressi a un alto livello di astrazione al fine di consentire agli ingegneri del software di fornire un’interpretazione personale, che comporta sempre l’aggiunta di ulteriori requisiti. Tali aggiunte devono essere chiaramente documentate.

>ad esempio, non si è parlato esplicitamente di possibili conseguenze – che comunque non è necessario siano gestite – della modifica dei valori dei parametri di configurazione, né si è fissato – e non è indispensabile farlo – un numero massimo di tentativi entro cui le due parti di un baratto devono convergere nello stabilire luogo, data e ora dell’incontro in cui avviene lo scambio effettivo degli articoli; inoltre, non si è stabilita la lunghezza massima, in termini di numero di caratteri, delle stringhe attraverso cui il fruitore esprime il valore dei campi atti a descrivere un articolo

## Richieste

Agli studenti è richiesto di realizzare evolutivamente cinque versioni software che soddisfino irequisiti sopra esposti.

Ogni gruppo (costituito al più da tre persone),  dovrà:

1) per ogni versione, produrre la documentazione di progetto, contenente
   - casi d’uso (comprensivi dell’espressione di eventuali requisiti aggiuntivi), sia in forma testuale, sia in forma di diagramma UML; si invita a rendere evidenti a colpo l’occhio le integrazioni/modifiche apportate ai casi d’uso (testuali e grafici) della versione precedente per ottenere quelli della versione corrente;
   - diagramma UML delle classi
   - diagrammi UML comportamentali (opzionali)
   - qualsiasi altra specifica ritenuta opportuna; la documentazione relativa alle cinque versioni deve essere raccolta in **un unico file**
2) per l’ultima versione, redigere un unico manuale di installazione e uso (il cui contenuto potrebbe eventualmente divenire parte dell’help in linea dell’applicazione); **si sottolinea la necessità di documentare accuratamente il da farsi al fine di  importare nell’applicazione le gerarchie di categorie e i dati di configurazione**
3) consegnare in formato elettronico quanto richiesto ai punti precedenti;
4) per ogni versione, consegnare codice sorgente + codice interpretabile + (preferibilmente) codice eseguibile.
