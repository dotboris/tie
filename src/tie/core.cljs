(ns tie.core)

(defn- for-input [val]
  (or val ""))

(defn- from-input [val]
  (when-not (empty? val) val))

(defn- props [m & kvs]
  (let [without-atom (dissoc m :atom)]
    (apply assoc without-atom kvs)))

(defn text
  "A data-bound textbox.

  It takes a single map of properties.
    `:atom` is required and is an atom that the control is data-bound to
    Extra properties are passed to the underlying input control

  Note that empty strings will be converted to `nil` and vice-versa.

  Example:
    [t/text {:atom my-atom}]"
  [{:keys [atom] :as ps}]
  [:input (props ps
            :type "text"
            :on-change #(let [val (.. % -target -value)]
                          (reset! atom (from-input val)))
            :value (for-input @atom))])

(defn textarea
  "A data-bound textarea (multiline textbox).

  It takes a single map of properties.
    `:atom` is required and is an atom that the control is data-bound to
    Extra properties are passed to the underlying input control

  Note that empty strings will be converted to `nil` and vice-versa.

  Example:
    [t/textarea {:atom my-atom}]"
  [{:keys [atom] :as ps}]
  [:textarea (props ps
                :type "text"
                :on-change #(let [val (.. % -target -value)]
                              (reset! atom (from-input val)))
                :value (for-input @atom))])

(defn checkbox
  "A data-bound checkbox.

  It takes a single map of properties.
    `:atom` is required and is an atom that the control is data-bound to.
    Extra properties are passed to the underlying input control

  The checkbox binds to that a boolean. If the atom is true, the checkbox will
  be checked. If the atom is false, the checkbox will be unchecked.
  Respectively, the atom value will be changed depending on the state of the
  checkbox.

  Example:
    [t/checkbox {:atom my-atom}]"
  [{:keys [atom] :as ps}]
  [:input (props ps
            :type "checkbox"
            :checked @atom
            :on-change #(swap! atom not))])

(defn radio
  "A data-bound radio button.

  It takes a single map of properties.
    `:atom` is required and is an atom that the control is data-bound to.
    `:value` is required and is the value that atom will take when the radio
      button is selected.
    Extra properties are passed to the underlying input control

  Radio buttons are meant to be used as a group. Radio buttons are part of the
  same logical group if they share the same atom. Radio buttons in the same
  logical group are meant to have different values.

  Example:
    [t/radio {:atom my-color :value \"red\"}]
    [t/radio {:atom my-color :value \"green\"}]
    [t/radio {:atom my-color :value \"blue\"}]"
  [{:keys [atom value] :as ps}]
  [:input (props ps
            :type "radio"
            :on-change #(reset! atom value)
            :checked (= value @atom))])

(defn select
  "A data-bound select (dropdown).

  It takes a single map of properties.
    `:atom` is required and is an atom that the control is data-bound to.
    `:options` is required and is a vector of options. Each option is a pair.
      The first (or left) value is the actual value of the option and the
      second (or right) value is the displayed value of the option.
    `:allow-nil` when set to `true`, a blank (or `nil`) option will be added to
      the select.
    `:nil-text` the display value of the blank / `nil` option (when set).
      Default to empty string.
    Extra properties are passed to the underlying input control

  Example:
    [t/select {:atom my-atom
               :options [[\"red\" \"Red\"]
                         [\"green\" \"Green\"]
                         [\"blue\" \"Blue\"]]
               :allow-nil true
               :nil-text \"Please select one\"}]"
  [{:keys [atom options allow-nil nil-text] :as ps}]
  [:select (props (dissoc ps :options :allow-nil :nil-text)
              :value (for-input @atom)
              :on-change #(let [val (.. % -target -value)]
                            (reset! atom (from-input val))))
    (when allow-nil
      [:option {:value ""} nil-text])
    (for [[val text] options]
      ^{:key val} [:option {:value val} text])])
