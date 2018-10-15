# How To Contribute

Thanks for reading our contribution guidelines.

The following guidelines for contribution should be followed if you want to report an issue or submit a pull request.


## How to submit an issue

* You need a [GitHub account](https://github.com/signup/free).
* Before reporting a bug or suggesting an improvement, please check the `develop` branch to see if it has already been addressed.
* Duplicate tickets will be closed without hesitation, so please check through existing tickets first to see if someone else has already discussed it.
* Submit an [issue ticket] for your issue if there is not one yet.
    * Describe the issue and include steps to reproduce if it's a bug.
    * If the issue is a bug, add any relevant Java or VirtualBox error messages. For Java error messages, a stacktrace can be provided. For VirtualBox you may provide the relevent lines of the [VirtualBox logs](https://blogs.oracle.com/scoter/virtualbox-log-files-v2).
    * Include relevant version numbers. For Java, you may use `$ java -v` to check the version. For VirtualBox, you may use `$ VBoxManage -v`. 
    * Additional screenshots or videos are often helpful.
* If you are able and want to fix this, fork the repository on GitHub.


## How To Fork the Repositories

Before making a change, you must fork the repository and perform the changes in your own copy of the code.  We use multiple git submodules (multiple repositories). In addition, we use the [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) branching model. The following are instructions to create the fork-repositories.

* Install the software prerequisites:
    * Install git: `$ sudo apt-get install git`
    * Install java 8: `$ sudo apt-get install openjdk-8-jdk`
    * Install launch4j. Download the [software](http://launch4j.sourceforge.net/), and install it into the `/opt` directory.
    * Install git-flow tools: `$ sudo apt-get install git-flow`
* Clone and initialize the repository into a local directory
    * Clone the parent repository: `$ git clone https://github.com/hyperbox/hyperbox.git`
    * Go to the repository directory: `$ cd hyperbox`
    * Initialize the submodules, using the `init` script: `$ ./init`
    * Create the fork directories: `$ ./fork <username> --create`, where `<username>` is your Github account name.
    * Initialize the git-flow tools: `$ git flow init -d`
* Build the software
    * Run the gradle task: `$ ./gradlew build`


## Make Changes

We use the [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) branching model. In this model, there are different branches for each change or bugfix, and a `develop` branch where multiple changes can be consolidated before creating a new release.  
Before reporting a bug or new feature, please check the `develop` branch to see if it's already been addressed.

The following are instructions to create a change, before making a pull request.

* Define the name for the new feature or bugfix branch
    * For enhancements, name the feature according to the feature e.g. `auto-activate`.
    * For un-reported bug fixes, add a `fix-` prefix to the feature name e.g. `fix-admin-notices`.
    * For code that addresses an existing Issue, add the Issue number as a prefix e.g. `feature/123-auto-activate` or `feature/321-fix-admin-notices`.
* In your forked repository, create a topic branch for your upcoming patch. 
    * You may create a feature branch using git-flow: `$ git flow feature start <name>`, where `<name>` is the name of the new feature.
    * You may create a bugfix branch using git-flow: `$ git flow bugfix start <name>`, where `<name>` is the name of the bugfix.
    * Please avoid working directly on the `develop` branch.
* Make commits of logical units and describe them properly.


## Commit Messages

We suggest you follow best practices for commit messages:

* Separate subject (first line) from body with a blank line.
* Limit the subject line to 50 characters.
* Capitalize the subject line.
* Do not end the subject line with a period.
* Use the imperative mood in the subject line.
* Wrap the body at 72 characters.
* Use the body to explain _what_ and _why_ versus _how_.
* Please [reference any existing issue](https://help.github.com/articles/closing-issues-via-commit-messages/) in the commit message.

Read [this post](http://chris.beams.io/posts/git-commit/) for more detail.


## Submit Changes

* Push your changes to a topic branch in your fork of the repository.
    * For a feature: `$ git flow feature publish <name>`
    * For a bugfix: `$ git flow bugfix publish <name>`
* Open a pull request to the original repository and choose the correct original branch (probably `develop`) you want to patch.
    * When you execute `git flow ... publish`, the command shows the URL to perform the pull request. You may copy the URL and use it in a browser to open the pull request.
* If you have administrative access to the repository, do not close any issue you referenced in your commit message.
    * The issue must be closed after reviewing and merging the change, but not by only opening the pull request.
* If you have write access to the repository, do not directly push or merge your own pull-requests. Let another team member review your pull request and approve.


## License

All submissions are agreed to be licensed under the same license as present in the repository. Please chek the LICENSE file in the repository.


## Security

There is no need to sign-off or GPG sign your commits. 


# Additional Resources

* [General GitHub documentation](http://help.github.com/)
* [GitHub pull request documentation](http://help.github.com/send-pull-requests/).
* [Read the Issue Guidelines by @necolas](https://github.com/necolas/issue-guidelines/blob/master/CONTRIBUTING.md) for more details,

[issue ticket]: https://github.com/hyperbox/hyperbox/issues
