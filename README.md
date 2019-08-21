# todos

## Start

```bash
git clone git@github.com:your-user-name/todos.git
cd todos
make db/create
make db/migrate
```

## Dev

```bash
make repl # starts an nrepl server
```

```clojure
; in your editor, connect to the nrepl server
(require '[server])
(in-ns 'server)
(-main)
```

```bash
curl http://localhost:1337 # or just open it in your browser
```

## Ship
```bash
make db/migrate
make assets
make uberjar
java -jar target/todos.jar
```
