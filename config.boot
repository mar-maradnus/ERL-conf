firewall {
    all-ping enable
    broadcast-ping disable
    ip-src-route disable
    log-martians enable
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
    name allow-all {
        default-action accept
        rule 1 {
            action accept
            state {
                established enable
                related enable
            }
        }
        rule 2 {
            action drop
            log enable
            state {
                invalid enable
            }
        }
    }
    name allow-est-drop-inv {
        default-action drop
        enable-default-log {
        }
        rule 1 {
            action accept
            state {
                established enable
                related enable
            }
        }
        rule 2 {
            action drop
            log enable
            state {
                invalid enable
            }
        }
    }
    name lan-local {
        default-action drop
        enable-default-log {
        }
        rule 1 {
            action accept
            state {
                established enable
                related enable
            }
        }
        rule 2 {
            action drop
            log enable
            state {
                invalid enable
            }
        }
        rule 100 {
            action accept
            protocol icmp
        }
        rule 600 {
            action accept
            description "Allow DNS"
            destination {
                port 53
            }
            protocol tcp_udp
        }
        rule 700 {
            action accept
            description "Allow DHCP"
            destination {
                port 67,68
            }
            protocol udp
        }
    }
    name local-WAN {
        default-action drop
        enable-default-log {
        }
        rule 1 {
            action accept
            state {
                established enable
                related enable
            }
        }
        rule 2 {
            action drop
            log enable
            state {
                invalid enable
            }
        }
        rule 100 {
            action accept
            protocol icmp
        }
        rule 400 {
            action accept
            description "Allow NTP"
            destination {
                port 123
            }
            protocol udp
        }
        rule 600 {
            action accept
            description "Allow DNS"
            destination {
                port 53
            }
            protocol tcp_udp
        }
        rule 700 {
            action accept
            description "Allow DHCP"
            destination {
                port 67,68
            }
            protocol udp
        }
    }
    name WAN-local {
        default-action drop
        enable-default-log {
        }
        rule 1 {
            action accept
            state {
                established enable
                related enable
            }
        }
        rule 2 {
            action drop
            log enable
            state {
                invalid enable
            }
        }
        rule 50 {
            action accept
            description "Allow OpenVPN connections"
            destination {
                port 443
            }
            protocol tcp
        }
    }
}
interfaces {
    ethernet eth0 {
        address dhcp
        description WAN
        duplex auto
        speed auto
    }
    ethernet eth1 {
        duplex auto
        speed auto
    }
    ethernet eth2 {
        address 192.168.200.1/24
        description LAN
        duplex auto
        speed auto
    }
    loopback lo
}
service {
    dhcp-server {
        disabled false
        hostfile-update disable
        shared-network-name LAN {
            authoritative enable
            subnet 192.168.200.0/24 {
                default-router 192.168.200.1
                dns-server 192.168.200.1
                lease 86400
                start 192.168.200.150 {
                    stop 192.168.200.160
                }
            }
            authoritative disable
        }
        use-dnsmasq disable
    }
    dns {
        forwarding {
            cache-size 150
            listen-on eth2
        }
    }
    gui {
        http-port 80
        https-port 443
    }
    nat {
        rule 5010 {
            description "Masquerade for WAN"
            outbound-interface eth0
            type masquerade
        }
    }
    ssh {
        port 22
        protocol-version v2
    }
}
system {
    domain-name xxxxxx.xx
    host-name xxxx
    login {
        user xxx {
            authentication {
                encrypted-password XXXXXXXXXX
                plaintext-password ""
            }
            full-name "XXX XXXXXXXX"
            level admin
        }
    }
    name-server 8.8.8.8
    name-server 8.8.4.4
    ntp {
        server 0.ubnt.pool.ntp.org
        server 1.ubnt.pool.ntp.org
        server 2.ubnt.pool.ntp.org
        server 3.ubnt.pool.ntp.org
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone UTC
    config-management {
        commit-revisions 20
    }
    conntrack {
    }
    console {
        device ttyS0 {
            speed 9600
        }
    }
    offload {
        ipsec enable
        ipv4 {
            forwarding enable
        }
        ipv6 {
            forwarding disable
        }
    }
}

/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "config-management@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@5:nat@3:qos@1:quagga@2:system@4:ubnt-pptp@1:ubnt-util@1:vrrp@1:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: v1.9.1.4939093.161214.0705 */
