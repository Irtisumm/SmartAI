git config user.email "irtisumjahid@gamil.com"
git config user.name "irtisum"
git init
git add .
git commit -m "Initial commit"
if ! git remote | grep -q origin; then
  git remote add origin https://github.com/irtisum/CityAI.git
fi
git push -u origin main
