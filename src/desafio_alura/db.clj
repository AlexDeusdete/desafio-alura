(ns desafio-alura.db
  (:require [java-time :as jtime]
            [desafio-alura.model :as model]
            [schema.core :as s]
            [datomic.api :as d]
            [clojure.walk :as walk]))

(def db-uri "datomic:dev://localhost:4334/loja")

(defn abre-conexao! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco! []
  (d/delete-database db-uri))

(defn dissoc-db-id [entidade]
  (if (map? entidade)
    (dissoc entidade :db/id)
    entidade))

(defn convert-to-localdate
  [date]
  (let [instant-with-zone (.atZone date (jtime/zone-id))]
    (jtime/local-date instant-with-zone)))

(defn convert-inst-to-localdate [entidade]
  (if (= (class entidade) java.util.Date)
    (convert-to-localdate (.toInstant entidade))
    entidade))


(defn convert-localdate-to-inst [entidade]
  (if (= (class entidade) java.time.LocalDate)
    (jtime/sql-date entidade)
    entidade))

(defn datomic-para-entidade [entidades]
  (walk/prewalk (comp convert-inst-to-localdate dissoc-db-id) entidades))

(defn entidade-para-datomic [entidades]
  (walk/prewalk convert-localdate-to-inst entidades))

(def schema [; Cartao
             {:db/ident       :cartao/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :cartao/tipo
              :db/valueType   :db.type/keyword
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cartao/numero
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cartao/cvv
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cartao/validade
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cartao/limite
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one}

             ; Cliente
             {:db/ident       :cliente/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :cliente/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cliente/cpf
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cliente/e-mail
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cliente/cartoes
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many}

             ;Compra
             {:db/ident       :compra/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :compra/data
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one}
             {:db/ident       :compra/valor
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one}
             {:db/ident       :compra/estabelecimento
              :db/valueType   :db.type/keyword
              :db/cardinality :db.cardinality/one}
             {:db/ident       :compra/categoria
              :db/valueType   :db.type/keyword
              :db/cardinality :db.cardinality/one}
             {:db/ident       :compra/cliente
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/one}
             {:db/ident       :compra/cartao
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/one}
             ])

(defn cria-schema! [conn]
  (d/transact conn schema))

(s/defn adiciona-altera-cliente!
  [conn clientes :- [model/Cliente]]
  (d/transact conn (entidade-para-datomic clientes)))

(s/defn adiciona-altera-cartoes!
  [conn cartoes :- [model/Cartao]]
  (d/transact conn cartoes))

(s/defn adiciona-altera-compras!
  [conn compras :- [model/Compra]]
  (d/transact conn (entidade-para-datomic compras)))

(s/defn todos-os-clientes :- [model/Cliente]
  [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?e [* {:cliente/cartoes [*]}]) ...]
           :where [?e :cliente/nome]] db)))

(s/defn todas-as-compras :- [model/Compra]
  [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?e [* {:compra/cartao [*]} {:compra/cliente [:cliente/id :cliente/nome :cliente/cpf :cliente/e-mail]}]) ...]
           :where [?e :compra/id]] db)))

(defn maior-numero-de-compras-de-um-cliente
  [db]
  (d/q '[:find (max ?count-compras) .
         :in $
         :where [(q '[:find (count ?compra) ?cliente
                      :where [?e :compra/id ?compra]
                      [?e :compra/cliente ?cliente]] $) [[?count-compras _]]]] db))

(s/defn clientes-com-maior-numero-de-compras :- [model/Cliente]
  [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?cliente [:cliente/id :cliente/nome :cliente/cpf :cliente/e-mail]) ...]
           :in $
           :where [?e :compra/id ?compra]
                  [?e :compra/cliente ?cliente]
           [(q '[:find (max ?count-compras) .
                :in $
                :where [(q '[:find (count ?compra) ?cliente
                             :where [?e :compra/id ?compra]
                             [?e :compra/cliente ?cliente]] $) [[?count-compras _]]]] $) ?count-compras]
           [(q '[:find (count ?e) .
                 :in $ ?cliente
                 :where [?e :compra/cliente ?cliente]] $ ?cliente) ?count]
           [(= ?count-compras ?count)]] db)))

(s/defn clientes-que-nunca-realizaram-compras :- [model/Cliente]
  [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?e [:cliente/id :cliente/nome :cliente/cpf :cliente/e-mail]) ...]
           :where [?e :cliente/nome]
           (not [_ :compra/cliente ?e])] db)))

(s/defn clientes-com-a-compra-de-maior-valor :- [model/Cliente]
  [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?cliente [:cliente/id :cliente/nome :cliente/cpf :cliente/e-mail]) ...]
           :in $
           :where [(q '[:find (max ?maior-valor) .
                           :where [_ :compra/valor ?maior-valor]] $) ?valor]
           [?e :compra/valor ?valor]
           [?e :compra/cliente ?cliente]] db)))