## Interactive Development
make our system friendly to interactive development

`src/hello.clj`
```clj
(def service-map
  {::http/routes routes
   ::http/type :jetty
   ::http/port 3000})

(defn start []
  (http/start (http/create-server service-map)))

;; For interactive development
(defonce server (atom nil))

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                        (assoc service-map
                               ; tell Pedestal not to wait for the server to exit
                               ; set ::http/join? to true when running in production so that your main function does not exit and terminate the process.
                               ::http/join? false)))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))
```

The key ::http/join? does the trick. We can now run start-dev in our REPL sessions. Instead of waiting forever, the REPL thread now returns, prints the value of the server (an ugly mess!) and lets us continue interacting.

```
clj
Clojure 1.11.1
user=> (require 'hello)
nil
user=> (hello/start-dev)
```

## Query Params
Update hander
```clj
(defn handler [request]
  (let [name (get-in request [:query-params :name])]
    {:status 200 :body (str "Hello, " name "!")}))
```

```
user=> (require :reload 'hello)
nil
user=> (hello/restart)
```

Access http://127.0.0.1:3000/greet?name=Michael