(ns tie.core)

(defn- for-input [val]
  (or val ""))

(defn text [{:keys [atom]}]
  [:input {:type "text"
           :on-change identity
           :value (for-input @atom)}])
