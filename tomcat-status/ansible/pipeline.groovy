pipeline {
    agent any
    options {
        ansiColor('xterm')
        timestamps ()
        office365ConnectorWebhooks([
            [name: "Office 365", url: "https://kocsistem.webhook.office.com/webhookb2/e40b14ae-7417-433a-a79b-a191380007be@1e1aa76b-4b02-45f4-9417-2e13eb0da973/IncomingWebhook/1d2c2671f9bd4948a84004a0ac3f0d3c/8f2054df-8ba2-41c7-85cf-d18489cd099d", notifyBuildStart: true, notifyBackToNormal: true, notifyFailure: true, notifyRepeatedFailure: true, notifySuccess: true, notifyAborted: true]
        ])
    }
    tools {
        maven "maven"
    }
    parameters {
        choice choices: ['sandbox', 'jobs', 'stage', 'ALL_PREPROD', 'ALL_PROD', 'PROD_01', 'PROD_02', 'PROD_03'], description: 'Please select which ENVIRONMENT do you want to restart?', name: 'ENVIRONMENT'
		    choice choices: ['restart', 'stop', 'start',], description: 'Please select which ACTION do you want?', name: 'ACTION'
    }
        environment {
        ENV = ""
    }
    stages {
        stage("Status_Change") {
           steps {
               script {
					if (params.ENVIRONMENT == 'PROD_01') {
						    ENV= 'CIMB2B01PAPP'
							}
					else if (params.ENVIRONMENT == 'PROD_02') {
						    ENV = 'CIMB2B02PAPP'
							}
					else if (params.ENVIRONMENT == 'PROD_03') {
						    ENV = 'ANKB2B01PAPP'
							}
					else if (params.ENVIRONMENT == 'ALL_PROD') {
						    ENV = 'prod'
							}
					else if (params.ENVIRONMENT == 'ALL_PREPROD') {
						    ENV = 'preprod'
							}
					else { 
					    ENV = params.ENVIRONMENT
					}

                    ansiblePlaybook colorized: true, credentialsId: 'weblogictest_ksmw248', disableHostKeyChecking: true, forks: 5,  installation: 'Ansible', inventory: 'ansible/ansible_hosts', playbook: 'ansible/tomcat_status.yml',
                    extras: "-t ${params.ACTION} -e HOST_GROUP=${ENV}"
                        
                }
                
            }    
        }
    } }
	
