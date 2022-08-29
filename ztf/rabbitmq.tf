resource "kubernetes_secret" "rabbitmq-secret" {
  metadata {
    name = "rabbitmq-secret"
    namespace = "mybatis"
  }
  type = "Opaque"
  binary_data = {
    rabbitmq-user = "ZGVmYXVsdF91c2VyX3RSVWdpYjRfZV9leFg0al84V3g="
#    default_user_tRUgib4_e_exX4j_8Wx
#    KoOiTweC0gZVOaQB7z5rfl3xD2ykVJPj
    #    rabbitmq-user = "ZGVmYXVsdF91c2VyX19SY28yeTJfRmNaUVhuM3N2SnQ="
    rabbitmq-password = "S29PaVR3ZUMwZ1pWT2FRQjd6NXJmbDN4RDJ5a1ZKUGo="
    #    rabbitmq-password = "dk1sLWJ2dWNuTHZ2bEVlWlU4YzZuNV9Qbzg0d0V6enM="
  }
}

resource "kubernetes_manifest" "rabbitmq-cluster" {
  manifest = {
    apiVersion: "rabbitmq.com/v1beta1"
    kind: "RabbitmqCluster"
    metadata = {
      name = "rabbitmq-service"
      namespace = "mybatis"
    }
    spec = {
      replicas: "3"
      resources = {
        limits = {
          cpu = "1"
          memory = "1Gi"
        }
      }
      service = {
        type = "ClusterIP"
      }
    }
  }
}

resource "kubernetes_manifest" "rabbitmq-policy" {
  manifest = {
    apiVersion = "rabbitmq.com/v1beta1"
    kind = "Policy"
    metadata = {
      name = "ha-policy"
      namespace = "mybatis"
    }
    spec = {
      name = "transient"
      vhost = "/"
      pattern = ""
      applyTo = "all"
      definition = {
        ha-mode = "all"
        ha-sync-mode = "automatic"
      }
      rabbitmqClusterReference = {
        name = "rabbitmq-service"
      }
    }
  }
}

#module "rabbitmq-cluster" {
#  source  = "mateothegreat/rabbitmq-cluster/kubernetes"
#  version = "2.0.23"
#  default_password = "rabbitmq"
#  default_username = "rabbitmq"
#  namespace = "mybatis"
#  name = "rabbitmq-service"
#  labels = {
#    "spotinst.io/restrict-scale-down" = "true"
#  }
#  limit_cpu        = "1"
#  limit_memory     = "1Gi"
#  replicas         = 3
#  role = "infra"
#}