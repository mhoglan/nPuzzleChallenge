# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.


  config.vm.define "box1" do |box1|
    box1.vm.box = "centos7-docker-2"
    box1.vm.network "private_network", type: "dhcp"
    box1.vm.provider "virtualbox" do |vb|
      vb.customize ["createhd",  "--filename", "btrfs_disk0", "--size", "10240"]
      vb.customize ["storageattach", :id, "--storagectl", "SATA Controller", "--port", "1", "--type", "hdd", "--medium", "btrfs_disk0.vdi"]
    end

    # For some reason chaining the systemctl commands with the mount commands·
    # results in docker not coming up correctly
    # Will be solved when moving provisioning to Ansible
    box1.vm.provision "shell",
    inline: "systemctl stop docker"

    box1.vm.provision "shell",
    inline: "mkfs.btrfs /dev/sdb && mount /dev/sdb /var/lib/docker"

    box1.vm.provision "shell",
    inline: "systemctl start docker"
  end
end
