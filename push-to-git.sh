# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit changes
git commit -m "Initial commit"

# Add remote repository (replace <github-repo-url> with your actual GitHub repository URL)
if ! git remote | grep -q origin; then
  git remote add origin <github-repo-url>
fi

# Push to remote repository (main branch)
git push -u origin main
