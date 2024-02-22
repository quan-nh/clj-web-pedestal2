(ns hello
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn handler [request]
  (let [name (get-in request [:query-params :name])]
    {:status 200 :body (str "Hello, " name "!")}))

(def routes
  (route/expand-routes
    #{["/greet" :get handler :route-name :greet]}))

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
