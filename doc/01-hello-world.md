## Starting From Scratch

    mkdir clj-web-pedestal

`deps.edn`
```clj
{:deps
 {io.pedestal/pedestal.jetty   {:mvn/version "0.6.1"}
  org.slf4j/slf4j-simple       {:mvn/version "2.0.9"}}
 :paths ["src"]}
```

`src/hello.clj`
```clj
(ns hello
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn handler [request]
  {:status 200 :body "Hello, world!"})

(def routes
  (route/expand-routes
    #{["/greet" :get handler :route-name :greet]}))

(defn create-server []
  (http/create-server
    {::http/routes routes
     ::http/type :jetty
     ::http/port 3000}))

(defn start []
  (http/start (create-server)))
```

start the app
```
clj
Clojure 1.11.1
user=> (require 'hello)
nil
user=> (hello/start)
```

App can be accessed at http://127.0.0.1:3000/greet
