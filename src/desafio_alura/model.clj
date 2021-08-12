(ns desafio-alura.model
  (:require [schema.core :as s]
            [java-time :as jtime]))

(defn uuid [] (java.util.UUID/randomUUID))

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Cartao
  {:cartao/id       java.util.UUID
   :cartao/tipo     s/Keyword
   :cartao/numero   s/Str
   :cartao/cvv      PosInt
   :cartao/validade java.time.LocalDate
   :cartao/limite   BigDecimal})

(def Cliente
  {:cliente/id                       java.util.UUID
   :cliente/nome                     s/Str
   :cliente/cpf                      s/Str
   :cliente/e-mail                   s/Str
   (s/optional-key :cliente/cartoes) [Cartao]})

(def Compra
  {:compra/id              java.util.UUID
   :compra/data            java.time.LocalDate
   :compra/valor           BigDecimal
   :compra/estabelecimento s/Keyword
   :compra/categoria       s/Keyword
   :compra/cliente         Cliente
   :compra/cartao          Cartao})

(s/defn novo-cartao :- Cartao
  ([tipo numero cvv validade limite]
   (novo-cartao (uuid) tipo numero cvv validade limite))
  ([uuid tipo numero cvv validade limite]
   {:cartao/id       uuid
    :cartao/tipo     tipo
    :cartao/numero   numero
    :cartao/cvv      cvv
    :cartao/validade validade
    :cartao/limite   limite}))

(s/defn novo-cliente :- Cliente
  ([nome cpf e-mail]
   (novo-cliente (uuid) nome cpf e-mail))
  ([uuid nome cpf e-mail]
   {:cliente/id     uuid
    :cliente/nome   nome
    :cliente/cpf    cpf
    :cliente/e-mail e-mail})
  ([uuid nome cpf e-mail cartoes :- [Cartao]]
   (assoc (novo-cliente uuid nome cpf e-mail) :cliente/cartoes cartoes)))

(s/defn nova-compra :- Compra
  ([data valor estabelecimento categoria cliente :- Cliente cartao :- Cartao]
   (nova-compra (uuid) data valor estabelecimento categoria cliente cartao))
  ([uuid data valor estabelecimento categoria cliente :- Cliente cartao :- Cartao]
   {:compra/id              uuid
    :compra/data            data
    :compra/valor           valor
    :compra/estabelecimento estabelecimento
    :compra/categoria       categoria
    :compra/cliente         cliente
    :compra/cartao          cartao}))


