(ns tie.core)

(defn- for-input [val]
  (or val ""))

(defn- from-input [val]
  (when-not (empty? val) val))

(defn- props [m & kvs]
  (let [without-atom (dissoc m :atom)]
    (apply assoc without-atom kvs)))

(defn text [{:keys [atom] :as ps}]
  [:input (props ps
            :type "text"
            :on-change #(let [val (.. % -target -value)]
                          (reset! atom (from-input val)))
            :value (for-input @atom))])

(defn textarea [{:keys [atom] :as ps}]
  [:textarea (props ps
                :type "text"
                :on-change #(let [val (.. % -target -value)]
                              (reset! atom (from-input val)))
                :value (for-input @atom))])

(defn checkbox [{:keys [atom] :as ps}]
  [:input (props ps
            :type "checkbox"
            :checked @atom
            :on-change #(swap! atom not))])

(defn radio [{:keys [atom value] :as ps}]
  [:input (props ps
            :type "radio"
            :on-change #(reset! atom value)
            :checked (= value @atom))])
