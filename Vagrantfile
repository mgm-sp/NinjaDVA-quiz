# -*- mode: ruby -*-
# vi: set ft=ruby :

#ENV["CURL_CA_BUNDLE"] = "mgmRootCertificateAuthority.pem"

Vagrant.configure("2") do |config|

	config.vm.box = "bento/debian-10"
	config.vbguest.auto_update = false

	#config.vm.box = "mgmsp/debian-minimal"
	#config.vm.box_url = "https://sp-vm-repo.mgm-edv.de/vagrant/debian-minimal.json"

	#config.vm.provision "shell", inline: <<-END
		#apt-get purge -y resolvconf
	#END

	config.vm.synced_folder ".", "/vagrant", disabled:true

	config.vm.define "quiz" do |m|# {{{ main Web Interface
		m.vm.hostname = "quiz"

		m.vm.synced_folder 'www', '/var/www/html'

		m.vm.provision :shell, inline: <<-END
			apt-get update -y
			apt-get install -y apache2 libapache2-mod-php php-soap
			# add php version to output for easier exploitation
			sed -e 's/^expose_php *=.*/expose_php = On/g' /etc/php/7.3/apache2/php.ini
			cd /var/www/html/
			service apache2 force-reload
		END
		require "erb"
		["dashboard-widgets/quiz"].each{|basename|
			#File.open("ninjadva/#{basename}.html","w"){|target| target << ERB.new(File.read("ninjadva/#{basename}.html.erb")).result }
		}
	end #}}}

	config.vm.define "quiz-api" do |m|# {{{ Webservice (2nd tier)
		m.vm.box = "bento/debian-9"
		m.vm.hostname = "quiz-api"

		m.vm.synced_folder 'api', '/var/local/api/'

		m.vm.provider "virtualbox" do |vb|
			vb.memory = "1536"
			vb.cpus = 2
		end

		m.vm.provision "shell", inline: <<-END
			apt-get -y update
			apt-get install -y maven default-jdk-headless tmux
			cd /var/local/api
			mvn package -DskipTests
		END
		m.vm.provision "shell",
			run: "always",
			inline: "tmux new-session -d '/var/local/api/run.sh'"
	end#}}}

	config.vm.define "quiz-db" do |m|# {{{ Database Backend
		m.vm.provision "file", source: "./init.sql", destination: "/tmp/init.sql"
		m.vm.hostname = "quiz-db"

		databaseuser = "database_user"
		databasepass = "damn_secret_password"
		m.vm.provision :shell, inline: <<-END
			apt-get update -y
			apt-get install -y default-mysql-server

			mysql -u root -e "CREATE DATABASE IF NOT EXISTS quiz;"
			mysql -u root -e "CREATE USER '#{databaseuser}'@'%' IDENTIFIED BY '#{databasepass}';"
			mysql -u root -e "GRANT ALL PRIVILEGES ON quiz.* TO '#{databaseuser}'@'%' WITH GRANT OPTION;"

			mysql -u root -e quiz < /tmp/init.sql

			echo "[mysqld]\nbind-address = 0.0.0.0" > /etc/mysql/mariadb.conf.d/99-quiz-db.cnf
			service mysqld restart
		END
	end #}}}

	###{{{ NinjaDVA specific configuration
	if File.exists?("../ninjadva.rb")
		require "../ninjadva"
		NinjaDVA.new(config)
	end#}}}
end

