# GitHub Actions Setup - Quick Start

## What Was Created

The following files enable automatic website building and deployment:

```
.github/
├── workflows/
│   └── build-website.yml      # GitHub Actions workflow (auto-triggers on push)
└── DEPLOYMENT.md              # Complete deployment documentation
```

## 3-Step Setup

### Step 1: Push to GitHub

```bash
git add .
git commit -m "Add Quarto website and GitHub Actions workflow"
git push origin main
```

### Step 2: Enable GitHub Pages

1. Go to your repository on GitHub
2. Click **Settings** (top right)
3. Click **Pages** in the left sidebar
4. Under "Build and deployment":
   - **Source**: Select **"GitHub Actions"**
5. Click **Save**

### Step 3: Watch It Deploy

1. Go to **Actions** tab on GitHub
2. You should see "Build and Deploy Website" running
3. Wait for it to complete (usually 2-5 minutes)
4. Your site is now live at: `https://YOUR-USERNAME.github.io/damon-single-facility`

## How It Works

Every time you push to `main` or `master`:

1. ✅ GitHub Actions automatically triggers
2. ✅ Builds Javadoc (Java API docs)
3. ✅ Builds Quarto website
4. ✅ Deploys to GitHub Pages
5. ✅ Site updates within minutes

## Verify It Works

After deployment completes:

```bash
# Check Actions tab to see build logs
# Then visit your live site:
https://YOUR-USERNAME.github.io/damon-single-facility
```

## What Gets Deployed

The workflow automatically includes:

- 📄 All `.qmd` documentation pages
- 📚 Vignettes (examples)
- 🔗 Javadoc (if Java compilation succeeds)
- 🎨 Custom styling (CSS, HTML)
- 🔍 Search functionality

## Troubleshooting

### "Source is not set to GitHub Actions"

**Solution**: Go to Settings → Pages and select "GitHub Actions" as the source

### "Workflow failing to run"

**Solution**: Check the Actions tab for error logs. Common issues:
- Missing `.github/workflows/build-website.yml` file
- Branch name not matching (change `main` to `master` if needed)

### "Site not updating"

**Solution**:
1. Check Actions tab - did the workflow run?
2. Wait a few minutes for GitHub Pages to propagate
3. Hard refresh: `Ctrl+Shift+R` (or `Cmd+Shift+R` on Mac)

### "Workflow runs but Pages shows nothing"

**Solution**:
1. Go to Settings → Actions → General
2. Under "Workflow permissions", select "Read and write permissions"
3. Click Save
4. Re-run workflow from Actions tab

## Customize the Workflow

Edit `.github/workflows/build-website.yml` to:

- Change trigger branches
- Skip certain file types
- Run on a schedule
- Add notifications

See `DEPLOYMENT.md` for advanced options.

## Local Testing

Before pushing, test locally:

```bash
make website        # Build locally
make website-preview # Preview in browser
```

## GitHub Pages URL

Your site will be available at:

```
https://YOUR-USERNAME.github.io/damon-single-facility/
```

Or if using an organization repository:

```
https://ORG-NAME.github.io/damon-single-facility/
```

## Next Steps

1. ✅ Run: `git push origin main`
2. ✅ Go to repository Settings → Pages
3. ✅ Select "GitHub Actions"
4. ✅ Check Actions tab for build progress
5. ✅ Visit your live website!

## Summary

You now have:

- ✅ Professional Quarto website
- ✅ Automatic building on every push
- ✅ GitHub Pages deployment
- ✅ API documentation (Javadoc)
- ✅ Vignettes and analysis examples

Everything is ready to deploy! 🚀

---

For more details, see `DEPLOYMENT.md`
