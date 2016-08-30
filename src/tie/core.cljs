(ns tie.core)

(defn- for-input [val]
  (or val ""))

(defn- from-input [val]
  (when-not (empty? val) val))

(defn- props [m & kvs]
  (let [without-atom (dissoc m :atom)]
    (apply assoc without-atom kvs)))

(defn text [{:keys [atom]}]
  [:input {:type "text"
           :on-change #(let [val (.. % -target -value)]
                          (reset! atom (from-input val)))
           :value (for-input @atom)}])

(defn checkbox [{:keys [atom] :as ps}]
  [:input (props ps
            :type "checkbox"
            :checked @atom
            :on-change #(swap! atom not))])
