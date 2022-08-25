data "kubernetes_secret" "test" {
  metadata {
    name = "rabbitmq-secret"
  }
  type = "Opaque"
  binary_data = {
    rabbitmq-user = "ZGVmYXVsdF91c2VyX19SY28yeTJfRmNaUVhuM3N2SnQ="
    rabbitmq-password = "dk1sLWJ2dWNuTHZ2bEVlWlU4YzZuNV9Qbzg0d0V6enM="
  }
}