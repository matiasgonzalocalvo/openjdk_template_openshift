def setenv()
{
  devops.aws_config("AWS_DESA_CMF")
  env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
  env.ECR_ID="arch/jenkins-slave-centos"
  env.TAG1="latest"
}
return this;
