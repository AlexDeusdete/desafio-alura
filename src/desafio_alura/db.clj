(ns desafio-alura.db)

(def cliente1 {:id      999
               :nome    "Gabriela Lima"
               :cpf     "000.000.000.00"
               :e-mail  "gabi.lima@teste.com.br"
               :cartoes [{:id       999111
                          :tipo     :virtual
                          :numero   "0000 0000 0000 0001"
                          :cvv      111
                          :validade "2025-07"
                          :limite   2000.00}
                         {:id       999222
                          :tipo     :fisico
                          :numero   "0000 0000 0000 0002"
                          :cvv      222
                          :validade "2030-01"
                          :limite   9000.00}]})

(def cliente2 {:id      888
               :nome    "Alex Reis"
               :cpf     "111.111.111-11"
               :e-mail  "alex.reis@teste.com.br"
               :cartoes [{:id       888111
                          :tipo     :fisico
                          :numero   "0000 0000 0000 8881"
                          :cvv      881
                          :validade "2024-07"
                          :limite   2000.00}]})

(def cliente3 {:id      777
               :nome    "Giovana Lima"
               :cpf     "222.222.222-22"
               :e-mail  "gi.lima@teste.com.br"
               :cartoes [{:id       777222
                          :tipo     :fisico
                          :numero   "0000 0000 0000 7772"
                          :cvv      772
                          :validade "2033-01"
                          :limite   19000.00}]})

(def compra1 {:id              1
              :data            "2021-07-20"
              :valor           500.00
              :estabelecimento :super-mercado-extra
              :categoria       :alimentacao
              :cliente (dissoc cliente1 :cartoes)
              :cartao-id       (get-in cliente1 [:cartoes 0])})

(def compra2 {:id              2
              :data            "2021-07-01"
              :valor           900.00
              :estabelecimento :farma-sao-paulo
              :categoria       :saude
              :cliente (dissoc cliente1 :cartoes)
              :cartao-id       (get-in cliente1 [:cartoes 1])})

(def compra3 {:id              3
              :data            "2021-06-03"
              :valor           1500.00
              :estabelecimento :unip
              :categoria       :educacao
              :cliente (dissoc cliente2 :cartoes)
              :cartao-id       (get-in cliente2 [:cartoes 0])})

(defn todas-as-compras []
  [compra1 compra2 compra3])

(defn todos-os-clientes []
  [cliente1 cliente2 cliente3])