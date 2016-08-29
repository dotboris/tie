(ns tie.core)

(defn- for-input [val]
  (or val ""))

(defn- from-input [val]
  (when-not (empty? val) val))

(defn text [{:keys [atom]}]
  [:input {:type "text"
           :on-change #(let [val (.. % -target -value)]
                          (reset! atom (from-input val)))
           :value (for-input @atom)}])

(defn checkbox [{:keys [atom]}]
  [:input {:type "checkbox"
           :checked @atom
           :on-change #(do (println "hi")
                           (swap! atom not))}])
