resource "kubernetes_secret" "postgres_secret" {
  metadata {
    name = "postgres-secret"
    namespace = "mybatis"
  }

  data = {
    postgres-password = "postgres"

    postgres-user = "postgres"
  }

  type = "Opaque"
}

resource "kubernetes_config_map" "postgres_config" {
  metadata {
    name = "postgres-config"
    namespace = "mybatis"
  }

  data = {
    postgres-url = "jdbc:postgresql://postgres-service:5432/mybatis"
  }
}

resource "kubernetes_deployment" "mybatis_service_deployment" {
  metadata {
    name = "mybatis-service-deployment"
    namespace = "mybatis"

    labels = {
      app = "mybatis-service"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "mybatis-service"
      }
    }

    template {
      metadata {
        labels = {
          app = "mybatis-service"
        }
      }

      spec {
        container {
          name  = "mybatis-service"
          image = "test/mybatis-service"

          port {
            container_port = 9090
          }

          env {
            name = "DB_USER"

            value_from {
              secret_key_ref {
                name = "postgres-secret"
                key  = "postgres-user"
              }
            }
          }

          env {
            name = "DB_PASSWORD"

            value_from {
              secret_key_ref {
                name = "postgres-secret"
                key  = "postgres-password"
              }
            }
          }

          env {
            name = "DB_URL"

            value_from {
              config_map_key_ref {
                name = "postgres-config"
                key  = "postgres-url"
              }
            }
          }

          env {
            name = "RABBITMQ_USER"

            value_from {
              secret_key_ref {
                name = "rabbitmq-secret"
                key  = "rabbitmq-user"
              }
            }
          }

          env {
            name = "RABBITMQ_PASSWORD"

            value_from {
              secret_key_ref {
                name = "rabbitmq-secret"
                key  = "rabbitmq-password"
              }
            }
          }

          image_pull_policy = "Never"
        }
      }
    }
  }
}

resource "kubernetes_service" "mybatis_service_service" {
  metadata {
    name = "mybatis-service-service"
    namespace = "mybatis"
  }

  spec {
    port {
      protocol    = "TCP"
      port        = 9090
      target_port = "9090"
      node_port   = 30500
    }

    selector = {
      app = "mybatis-service"
    }

    type = "NodePort"
  }
}

