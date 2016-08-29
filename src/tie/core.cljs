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
