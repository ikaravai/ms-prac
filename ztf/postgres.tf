resource "kubernetes_stateful_set" "postgres_stateful_set" {
  metadata {
    name = "postgres-deployment"
    namespace = "mybatis"
    labels = {
      app = "postgres"
    }
  }
  spec {
    service_name = "postgres-service"
    replicas = 1
    revision_history_limit = 10
    selector {
      match_labels = {
        app = "postgres"
      }
    }
    template {
      metadata {
        labels = {
          app = "postgres"
        }
      }
      spec {
        container {
          name = "postgres"
          env {
            name = "POSTGRES_PASSWORD"
            value = "postgres"
          }
          env {
            name = "TZ"
            value = "Europe/Minsk"
          }
          image = "postgres:14.1-alpine"
          port {
            container_port = 5432
          }
          image_pull_policy = "IfNotPresent"
          termination_message_path = "/dev/termination-log"
          termination_message_policy = "File"
          volume_mount {
            mount_path = "/var/lib/postgresql/data"
            name       = "data"
          }
        }
        dns_policy = "ClusterFirst"
        restart_policy = "Always"
        termination_grace_period_seconds = 30
      }
    }
    volume_claim_template {
      metadata {
        name = "data"
      }
      spec {
        access_modes = ["ReadWriteOnce"]
        resources {
          requests = {
            storage = "1Gi"
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "postgres_service" {
  metadata {
    name = "postgres-service"
    namespace = "mybatis"
  }
  spec {
    type = "ClusterIP"
    cluster_ip = "None"
    selector = {
      app = "postgres"
    }
    port {
      port = 5432
      protocol = "TCP"
      target_port = 5432
    }
  }
}