# GitHub Pages Deployment Guide

## Automatic Website Building & Deployment

Your project is configured to automatically build and deploy a website whenever you push to GitHub!

## Setup Instructions

### 1. Enable GitHub Pages

1. Go to your repository on GitHub
2. Click **Settings** → **Pages** (in left sidebar)
3. Under "Build and deployment":
   - **Source**: Select "GitHub Actions"
   - This tells GitHub to use the `.github/workflows/build-website.yml` file
4. Click **Save**

### 2. Create a GitHub Token (if needed)

For manual deployments, you might need a token:

1. Go to GitHub → Settings → Developer settings → Personal access tokens
2. Generate a new token with `repo` and `pages:write` scopes
3. Save it securely

### 3. Push to GitHub

The workflow automatically triggers on:

```bash
git push origin main
```

## Workflow Details

The GitHub Actions workflow (`.github/workflows/build-website.yml`):

1. **Triggers on**:
   - Push to `main` or `master` branch
   - Manual trigger (`workflow_dispatch`)
   - Pull requests (for preview only, doesn't deploy)

2. **Builds**:
   - Compiles Java source code
   - Generates Javadoc API documentation
   - Renders Quarto website

3. **Deploys**:
   - Uploads built site to GitHub Pages
   - Makes available at: `https://YOUR-USERNAME.github.io/damon-single-facility`

## Monitoring the Build

### Watch the Build

1. Go to your repository on GitHub
2. Click **Actions** tab
3. Watch the latest workflow run in real-time
4. Each step shows logs and output

### Check Build Status

Add status badge to your README:

```markdown
[![Build and Deploy Website](https://github.com/YOUR-USERNAME/damon-single-facility/actions/workflows/build-website.yml/badge.svg)](https://github.com/YOUR-USERNAME/damon-single-facility/actions/workflows/build-website.yml)
```

## Troubleshooting

### Workflow Not Running

**Problem**: "build-website.yml not found" error

**Solution**: Make sure the file is at: `.github/workflows/build-website.yml`

### Build Fails: "Quarto not found"

**Problem**: Quarto isn't installed in the runner

**Solution**: Already handled! Workflow includes `quarto-dev/quarto-actions/setup@v2`

### Build Fails: Java/Javadoc Errors

**Problem**: Java compilation fails

**Solution**: Workflow is configured to continue even if Java compilation fails (with `continue-on-error: true`). To fix:

1. Check local compilation: `make compile`
2. Check for missing dependencies
3. Ensure Java 11 is being used

### Website Not Deploying

**Problem**: Build succeeds but site doesn't appear at GitHub Pages URL

**Solution**:
1. Verify Pages is enabled (Settings → Pages)
2. Check the Actions tab for deployment step
3. Wait a few minutes - GitHub Pages can take time to update
4. Hard refresh your browser (`Ctrl+Shift+R`)

### Deployment Permission Error

**Problem**: "Error: Permission denied" during deploy

**Solution**:
1. Go to Settings → Actions → General
2. Under "Workflow permissions", select "Read and write permissions"
3. Click Save
4. Re-run the workflow

## Customizing the Workflow

### Change Trigger Branches

Edit `.github/workflows/build-website.yml`:

```yaml
on:
  push:
    branches:
      - main
      - master
      - develop  # Add more branches
```

### Exclude Paths from Triggering

```yaml
on:
  push:
    branches:
      - main
    paths-ignore:
      - 'README.md'      # Don't trigger for README changes
      - '*.txt'          # Don't trigger for text files
      - 'new_analysis/**' # Don't trigger for R analysis folder
```

### Run on Schedule

Deploy website on a schedule (e.g., daily):

```yaml
on:
  schedule:
    - cron: '0 2 * * *'  # Run at 2 AM UTC daily
```

### Manual Workflow Trigger

The workflow already supports `workflow_dispatch`, so you can:

1. Go to **Actions** tab
2. Select **Build and Deploy Website**
3. Click **Run workflow**
4. Choose branch
5. Click **Run workflow**

## Viewing the Deployed Website

Once deployed, your website is available at:

```
https://YOUR-USERNAME.github.io/damon-single-facility
```

Or if using an organization repository:

```
https://ORG-NAME.github.io/damon-single-facility
```

## Website Content

The deployed website includes:

- 📄 Homepage (index.qmd)
- 📚 Full documentation (docs/)
- 🔬 Vignettes/examples (vignettes/)
- 🔗 Javadoc API docs (docs/api/)
- 🎨 Custom styling
- 🔍 Built-in search

## Local Testing

Before pushing, test locally:

```bash
# Build locally
make website

# Preview locally
make website-preview

# Open in browser
open _site/index.html
```

## Advanced: Custom Domain

To use a custom domain (e.g., `model.example.com`):

1. Update DNS records (CNAME or A records)
2. Go to Settings → Pages
3. Enter custom domain
4. GitHub creates a `CNAME` file automatically

See [GitHub docs](https://docs.github.com/en/pages/configuring-a-custom-domain-for-your-github-pages-site) for details.

## Advanced: Private Repository

If your repository is private:

1. GitHub Pages is available but requires Teams/Pro plan
2. Workflow still runs automatically
3. Site is only accessible to collaborators with read access
4. In Settings → Pages, choose visibility level

## CI/CD Integration

The workflow can be extended to:

- Run tests automatically
- Generate coverage reports
- Deploy to multiple hosting services
- Send notifications on failure

See example workflow modifications below.

## Example: Notification on Failure

```yaml
# Add to end of workflow
- name: Notify Slack on failure
  if: failure()
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "Website build failed: ${{ github.repository }}"
      }
```

## Frequently Asked Questions

**Q: How often does the website update?**
A: Whenever you push to main/master branch. Deployments typically complete in 1-2 minutes.

**Q: Can I view past deployments?**
A: Yes! Check the Actions tab to see all workflow runs and their logs.

**Q: What if I want to disable auto-deployment?**
A: Rename or delete `.github/workflows/build-website.yml` to disable.

**Q: Can I deploy to other hosting services?**
A: Yes! Modify the workflow to deploy to AWS S3, Netlify, etc.

## Getting Help

- [Quarto Documentation](https://quarto.org/docs/publishing/github-pages.html)
- [GitHub Actions Documentation](https://docs.github.io/en/actions)
- [GitHub Pages Documentation](https://docs.github.io/en/pages)

---

**Happy deploying!** 🚀
