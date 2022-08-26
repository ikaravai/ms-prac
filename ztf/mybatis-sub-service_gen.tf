resource "kubernetes_deployment" "mybatis_sub_service_deployment" {
  metadata {
    name = "mybatis-sub-service-deployment"
    namespace = "mybatis"

    labels = {
      app = "mybatis-sub-service"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "mybatis-sub-service"
      }
    }

    template {
      metadata {
        labels = {
          app = "mybatis-sub-service"
        }
      }

      spec {
        container {
          name  = "mybatis-sub-service"
          image = "test/mybatis-sub-service"

          port {
            container_port = 9091
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

resource "kubernetes_service" "mybatis_sub_service_service" {
  metadata {
    name = "mybatis-sub-service-service"
    namespace = "mybatis"
  }

  spec {
    port {
      protocol    = "TCP"
      port        = 9091
      target_port = "9091"
      node_port   = 30501
    }

    selector = {
      app = "mybatis-sub-service"
    }

    type = "NodePort"
  }
}

