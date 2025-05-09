<template>
    <div class="container w-50">
        <div class="row mt-5">
            <div class="col-md-12 text-center">
                <p v-if="form == undefined" class="fs-5">Cadastro de Modelo</p>
                <p v-if="form == 'editar'" class="fs-5">Editar Modelo</p>
                <p v-if="form == 'toggle' && modelo.ativo" class="fs-5">Desativar Modelo</p>
                <p v-if="form == 'toggle' && !modelo.ativo" class="fs-5">Ativar Modelo</p>
            </div>
            <div class="col-md-2"></div>
        </div>

        <AvisoComponent :ativo="mensagem.ativo" :sucesso="mensagem.status" :mensagem="mensagem.mensagem">
        </AvisoComponent>

        <div class="row w-100 d-flex justify-content-center m-0 mb-2">
            <div class="mb-3 mt-3 w-50 text-start">
                <label class="form-label">Nome do Modelo</label>
                <input type="text" :disabled="orm === 'toggle' ? '' : disabled" class="form-control"
                    v-on:keyup.enter="onClickCadastrar()" v-model="modelo.nome" />
            </div>
            <div class="mb-3 mt-3 w-50 text-start">
                <label for="categoria" class="form-label">Marca</label>
                <select :disabled="form === 'toggle' ? '' : disabled" class="form-select" id="categoria"
                    v-model="modelo.marca">
                    <option value="" selected>Selecione uma marca</option>
                    <option v-for="marca in marcas" :key="marca.id" :value="marca">
                        {{ marca.nome }}
                    </option>
                </select>
            </div>
        </div>

        <div class="row d-flex justify-content-center">
            <div class="col-md-3">
                <div class="d-grid gap-2">
                    <router-link type="button" class="btn btn-secondary" to="/modelo">Voltar
                    </router-link>
                </div>
            </div>
            <div class="col-md-3">
                <div class="d-grid gap-2">
                    <button v-if="form === undefined" type="button" class="btn btn-primary" @click="onClickCadastrar()">
                        Cadastrar
                    </button>
                    <button v-if="form === 'editar'" type="button" class="btn btn-warning" @click="onClickEditar()">
                        Editar
                    </button>
                    <button v-if="form === 'toggle' && modelo.ativo == true" type="button" class="btn btn-danger"
                        @click="onClickExcluir()">
                        Excluir
                    </button>
                    <button v-if="form === 'toggle' && modelo.ativo == false" type="button" class="btn btn-success"
                        @click="onClickAtivar()">
                        Ativar
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import AvisoComponent from '@/components/AvisoComponent.vue'
import { MarcaClient } from '@/service/Marca.client'
import { Marca } from '@/model/Marca'
import { defineComponent } from 'vue'
import { Modelo } from '@/model/Modelo'
import { ModeloClient } from '@/service/Modelo.client'

export default defineComponent({
    name: 'ModeloFormulario',
    data(): any {
        return {
            modeloClient: new ModeloClient(),
            modelo: new Modelo(),
            marcas: [] as Marca[],
            mensagem: {
                ativo: false as boolean,
                status: false as boolean,
                mensagem: '' as string
            }
        }
    },
    components: {
        AvisoComponent
    },
    computed: {
        id(): any {
            return this.$route.query.id
        },
        form(): any {
            return this.$route.query.form
        }
    },
    mounted() {
        if (this.id !== undefined) {
            this.findById(Number(this.id))
        }
        this.fetchMarcas()
    },
    methods: {
        fetchMarcas() {
            const marcaClient = new MarcaClient()
            marcaClient.listarAtivos().then(sucess => {
                this.marcas = sucess
            }).catch(error => {
                this.mensagem.mensagem = error.response.data
                this.mensagem.status = false
                this.mensagem.ativo = true
            })
        },
        onClickCadastrar() {
            const modeloClient = new ModeloClient()
            modeloClient
                .cadastrar(this.modelo)
                .then(sucess => {
                    this.modelo = new Modelo()
                    this.mensagem.mensagem = sucess
                    this.mensagem.status = true
                    this.mensagem.ativo = true
                })
                .catch(error => {
                    if (typeof (error.response.data) == 'object') {
                        this.mensagem.mensagem = Object.values(error.response.data)[0]
                    } else {
                        if (this.modelo.marca == "") {
                            this.mensagem.mensagem = "Selecione uma marca!"
                        } else {
                            this.mensagem.mensagem = error.response.data
                        }
                    }
                    this.mensagem.status = false
                    this.mensagem.ativo = true
                })
        },
        findById(id: number) {
            const modeloClient = new ModeloClient()
            modeloClient
                .findById(id)
                .then(sucess => {
                    this.modelo = sucess
                })
                .catch(error => {
                    this.mensagem.mensagem = error.response.data
                    this.mensagem.status = false
                    this.mensagem.ativo = true
                })
        },
        onClickEditar() {
            const modeloClient = new ModeloClient()
            modeloClient
                .editar(this.modelo)
                .then(sucess => {
                    this.mensagem.mensagem = sucess
                    this.mensagem.status = true
                    this.mensagem.ativo = true
                })
                .catch(error => {
                    if (this.modelo.marca == "") {
                        this.mensagem.mensagem = "Selecione uma marca!"
                    } else {
                        this.mensagem.mensagem = error.response.data
                    }
                    this.mensagem.status = false
                    this.mensagem.ativo = true
                })
        },
        onClickExcluir() {
            if (confirm("Tem certeza que deseja desativar esse modelo?")) {
                const modeloClient = new ModeloClient()
                modeloClient
                    .desativar(this.modelo.id)
                    .then(sucess => {
                        this.modelo.ativo = false
                        this.mensagem.mensagem = sucess
                        this.mensagem.status = true
                        this.mensagem.ativo = true
                    })
                    .catch(error => {
                        this.mensagem.mensagem = error.response.data
                        this.mensagem.status = false
                        this.mensagem.ativo = true
                    })
            }
        },
        onClickAtivar() {
            if (confirm("Tem certeza que reativar desativar esse modelo?")) {
                this.modelo.ativo = true
                const modeloClient = new ModeloClient()
                modeloClient
                    .editar(this.modelo)
                    .then(sucess => {
                        this.mensagem.mensagem = sucess
                        this.mensagem.status = true
                        this.mensagem.ativo = true
                    })
                    .catch(error => {
                        this.mensagem.mensagem = error.response.data
                        this.mensagem.status = false
                        this.mensagem.ativo = true
                    })
            }
        }
    }
})
</script>
