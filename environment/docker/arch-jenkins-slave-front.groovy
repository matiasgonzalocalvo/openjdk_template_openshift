def setenv()
{
  //devops.aws_config("AWS_DESA_CMF")
  devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
  env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
  env.ECR_ID="arch/${repoName}"
  env.TAG1="latest"
}
return this;
