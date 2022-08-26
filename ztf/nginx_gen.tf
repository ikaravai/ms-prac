resource "kubernetes_config_map" "nginx_config" {
  metadata {
    name = "nginx-config"
    namespace = "mybatis"
  }

  data = {
    authdetails = "admin:O26r2KIp4hZn6:thisisadmin\nuser:O26r2KIp4hZn6:normaluser\nroot:O26r2KIp4hZn6:devrootuser\n"

    "mime.types" = "types {\n  text/html                                        html htm shtml;\n  text/css                                         css;\n  text/xml                                         xml;\n  image/gif                                        gif;\n  image/jpeg                                       jpeg jpg;\n  application/javascript                           js;\n  application/atom+xml                             atom;\n  application/rss+xml                              rss;\n\n  text/mathml                                      mml;\n  text/plain                                       txt;\n  text/vnd.sun.j2me.app-descriptor                 jad;\n  text/vnd.wap.wml                                 wml;\n  text/x-component                                 htc;\n\n  image/avif                                       avif;\n  image/png                                        png;\n  image/svg+xml                                    svg svgz;\n  image/tiff                                       tif tiff;\n  image/vnd.wap.wbmp                               wbmp;\n  image/webp                                       webp;\n  image/x-icon                                     ico;\n  image/x-jng                                      jng;\n  image/x-ms-bmp                                   bmp;\n\n  font/woff                                        woff;\n  font/woff2                                       woff2;\n\n  application/java-archive                         jar war ear;\n  application/json                                 json;\n  application/mac-binhex40                         hqx;\n  application/msword                               doc;\n  application/pdf                                  pdf;\n  application/postscript                           ps eps ai;\n  application/rtf                                  rtf;\n  application/vnd.apple.mpegurl                    m3u8;\n  application/vnd.google-earth.kml+xml             kml;\n  application/vnd.google-earth.kmz                 kmz;\n  application/vnd.ms-excel                         xls;\n  application/vnd.ms-fontobject                    eot;\n  application/vnd.ms-powerpoint                    ppt;\n  application/vnd.oasis.opendocument.graphics      odg;\n  application/vnd.oasis.opendocument.presentation  odp;\n  application/vnd.oasis.opendocument.spreadsheet   ods;\n  application/vnd.oasis.opendocument.text          odt;\n  application/vnd.openxmlformats-officedocument.presentationml.presentation\n                                                   pptx;\n  application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\n                                                   xlsx;\n  application/vnd.openxmlformats-officedocument.wordprocessingml.document\n                                                   docx;\n  application/vnd.wap.wmlc                         wmlc;\n  application/wasm                                 wasm;\n  application/x-7z-compressed                      7z;\n  application/x-cocoa                              cco;\n  application/x-java-archive-diff                  jardiff;\n  application/x-java-jnlp-file                     jnlp;\n  application/x-makeself                           run;\n  application/x-perl                               pl pm;\n  application/x-pilot                              prc pdb;\n  application/x-rar-compressed                     rar;\n  application/x-redhat-package-manager             rpm;\n  application/x-sea                                sea;\n  application/x-shockwave-flash                    swf;\n  application/x-stuffit                            sit;\n  application/x-tcl                                tcl tk;\n  application/x-x509-ca-cert                       der pem crt;\n  application/x-xpinstall                          xpi;\n  application/xhtml+xml                            xhtml;\n  application/xspf+xml                             xspf;\n  application/zip                                  zip;\n\n  application/octet-stream                         bin exe dll;\n  application/octet-stream                         deb;\n  application/octet-stream                         dmg;\n  application/octet-stream                         iso img;\n  application/octet-stream                         msi msp msm;\n\n  audio/midi                                       mid midi kar;\n  audio/mpeg                                       mp3;\n  audio/ogg                                        ogg;\n  audio/x-m4a                                      m4a;\n  audio/x-realaudio                                ra;\n\n  video/3gpp                                       3gpp 3gp;\n  video/mp2t                                       ts;\n  video/mp4                                        mp4;\n  video/mpeg                                       mpeg mpg;\n  video/quicktime                                  mov;\n  video/webm                                       webm;\n  video/x-flv                                      flv;\n  video/x-m4v                                      m4v;\n  video/x-mng                                      mng;\n  video/x-ms-asf                                   asx asf;\n  video/x-ms-wmv                                   wmv;\n  video/x-msvideo                                  avi;\n}\n"

    "nginx.conf" = "http {\n\n    include mime.types;\n\n    upstream mybatis {\n        least_conn;\n        server mybatis-service-service:9090 max_fails=3 fail_timeout=1s weight=5;\n        server mybatis-sub-service-service:9091    backup;\n    }\n\n    proxy_cache_path /var/nginx/cache keys_zone=MYBATIS_CACHE:60m levels=1:2 inactive=3h max_size=20g;\n\n    server {\n        listen 80 default_server;\n        listen [::]:80 default_server;\n\n        root asd/qwe/qwe;\n\n        server_name mynginx.com www.mynginx.com;\n\n        add_header X-GG-Cache-Status $upstream_cache_status;\n\n        location /mybatis {\n            proxy_pass            http://mybatis;\n            proxy_set_header      X-Real-IP $remote_addr;\n            proxy_set_header      X-Forwarded-For $proxy_add_x_forwarded_for;\n            proxy_set_header      Host $http_host;\n            limit_rate_after      1m;\n            limit_rate            1m;\n            proxy_cache           MYBATIS_CACHE;\n            proxy_cache_valid     200 15s;\n            proxy_cache_use_stale error timeout invalid_header updating http_500 http_502 http_503 http_504;\n            proxy_cache_lock      on;\n            proxy_cache_lock_age  3s;\n            proxy_cache_lock_timeout 2s;\n            auth_basic            \"Mybatis auth setup\";\n            auth_basic_user_file  conf.d/authdetails;\n            allow                 172.17.0.0/24;\n            allow                 127.0.0.0/24;\n            allow                 127.0.0.1;\n            allow                 10.0.0.0/8;\n            deny                  all;\n        }\n\n        # Media: images, icons, video, audio, HTC\n        location ~* \\.(?:jpg|jpeg|gif|png|ico|cur|gz|svg|svgz|mp4|mp3|ogg|ogv|webm|htc|woff2|woff)$ {\n            expires 1d;\n            access_log off;\n            add_header Cache-Control \"max-age=86400, public\";\n        }\n\n        # CSS and Javascript\n        location ~* \\.(?:css|js)$ {\n            expires 1m;\n            access_log off;\n            add_header Cache-Control \"max-age=60, public\";\n        }\n    }\n}\n\nevents {\n\n}\n"
  }
}

resource "kubernetes_deployment" "nginx_deployment" {
  metadata {
    name = "nginx-deployment"
    namespace = "mybatis"

    labels = {
      app = "nginx"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "nginx"
      }
    }

    template {
      metadata {
        labels = {
          app = "nginx"
        }
      }

      spec {
        volume {
          name = "nginx-config"

          config_map {
            name = "nginx-config"

            items {
              key  = "nginx.conf"
              path = "nginx.conf"
            }

            items {
              key  = "mime.types"
              path = "mime.types"
            }

            items {
              key  = "authdetails"
              path = "conf.d/authdetails"
            }
          }
        }

        volume {
          name      = "log"
          empty_dir {}
        }

        container {
          name  = "nginx"
          image = "nginx:stable-alpine"

          port {
            container_port = 80
          }

          env {
            name  = "TZ"
            value = "Europe/Minsk"
          }

          volume_mount {
            name       = "nginx-config"
            read_only  = true
            mount_path = "/etc/nginx"
          }

          volume_mount {
            name       = "log"
            mount_path = "/var/nginx/cache"
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "nginx_service" {
  metadata {
    name = "nginx-service"
    namespace = "mybatis"
  }

  spec {
    port {
      protocol    = "TCP"
      port        = 9999
      target_port = 80
      node_port = 30100
    }

    selector = {
      app = "nginx"
    }

    type = "NodePort"
  }
}

