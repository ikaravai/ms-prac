resource "kubernetes_secret" "rabbitmq-secret" {
  metadata {
    name = "rabbitmq-secret"
    namespace = "mybatis"
  }
  type = "Opaque"
  binary_data = {
    rabbitmq-user = "ZGVmYXVsdF91c2VyX3VMZEE0Q0dqX25qLVFCVGZ6T3I="
#    default_user_uLdA4CGj_nj-QBTfzOr
#    -1N6Vg3DNIP0Khk_arIS1ys_jQPYLf9T
    #    rabbitmq-user = "ZGVmYXVsdF91c2VyX19SY28yeTJfRmNaUVhuM3N2SnQ="
    rabbitmq-password = "LTFONlZnM0ROSVAwS2hrX2FySVMxeXNfalFQWUxmOVQ="
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