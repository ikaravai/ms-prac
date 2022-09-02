resource "kubernetes_manifest" "elastic" {
  manifest = {
    apiVersion = "elasticsearch.k8s.elastic.co/v1"
    kind       = "Elasticsearch"
    metadata   = {
      name = "my-elastic"
      namespace = "mybatis"
    }
    spec = {
      version  = "8.4.1"
      nodeSets = {
        name   = "default"
        count  = 1
        config = {
          node = {
            store = {
              allow_mmap = false
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_manifest" "kibana" {
  manifest = {
    apiVersion = "kibana.k8s.elastic.co/v1"
    kind     = "Kibana"
    metadata = {
      name = "my-kibana"
      namespace = "mybatis"
    }
    spec = {
      version          = "8.4.1"
      count            = 1
      elasticsearchRef = {
        name = "my-elastic"
      }
    }
  }
}