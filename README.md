# XML Address Import

Jednoduchá Java aplikace, která stáhne zazipovaný XML soubor s katastrálními daty, zpracuje ho a uloží vybrané údaje (obec a její části) do PostgreSQL databáze.

---

## Co aplikace dělá

1. Stáhne ZIP soubor z veřejné URL (`https://www.smartform.cz/download/kopidlno.xml.zip`)
2. Rozbalí ho a najde uvnitř XML soubor
3. Naparsuje XML pomocí StAX a vytáhne z něj:
    - Obec (kód, název)
    - Části obce (kód, název, kód nadřazené obce)
4. Uloží data do PostgreSQL databáze

Aplikace se spustí, provede import a automaticky skončí – žádné REST API ani webové rozhraní.

---

## Tech stack

- Java 17 + Spring Boot 4.1
- Spring Data JPA / Hibernate
- PostgreSQL 16
- StAX (java.xml.stream) pro parsování XML
- Java HttpClient pro stažení souboru
- Docker (lokální databáze)
- Maven

---

## Jak to spustit

### 1. Naklonuj repo

```bash
git clone https://github.com/JinderO/xml-address-import.git
cd xml-address-import
```

### 2. Spusť databázi

```bash
docker-compose up -d
```

### 3. Spusť aplikaci

```bash
./mvnw spring-boot:run
```

Nebo přes tlačítko Run v IntelliJ IDEA.

Po spuštění appka automaticky stáhne data, zpracuje je a uloží do databáze. V konzoli uvidíš log o průběhu importu.

## Struktura projektu

```
com.jindero.xmlimport.xmladdressimport
├── entity        # JPA entity (Obec, CastObce)
├── repository    # Spring Data JPA repository
└── service       # Business logika
    ├── FileService       # Stažení a rozbalení ZIP souboru
    ├── XmlParser         # Parsování XML pomocí StAX
    ├── ParseResult       # DTO pro výsledek parsování
    └── DataImportRunner  # Spouští celý proces importu při startu appky
```

## Poznámky k návrhu

- **`kod` jako primární klíč** – obec i části obce mají přirozený, unikátní kód přímo z XML dat, takže nebylo potřeba generovat žádné umělé ID.
- **`kodObce` jako String, ne `@ManyToOne`** – pro tento import scénář stačí uložit referenční hodnotu. Plnohodnotný vztah mezi entitami by dával smysl, kdyby aplikace potřebovala navigovat mezi objekty (např. v rámci REST API).
- **StAX místo DOM** – XML soubor obsahuje kompletní katastrální data pro celou obec (desítky tisíc řádků), StAX umožňuje číst jen relevantní část bez zbytečné zátěže paměti.