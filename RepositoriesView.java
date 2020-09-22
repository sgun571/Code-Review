		public Object[] getChildren(Object parentElement) {

			RepositoryTreeNode node = (RepositoryTreeNode) parentElement;
			Repository repo = node.getRepository();

			switch (node.getType()) {

			case BRANCHES:

				List<RepositoryTreeNode<Ref>> refs = new ArrayList<RepositoryTreeNode<Ref>>();

				Repository rep = node.getRepository();
				for (Map.Entry<String, Ref> ref : rep.getAllRefs().entrySet()) {
					refs.add(new RepositoryTreeNode<Ref>(node,
							RepositoryTreeNodeType.REF, repo, ref.getValue()));
				}

				return refs.toArray();

			case REPO:

				List<RepositoryTreeNode<Repository>> branches = new ArrayList<RepositoryTreeNode<Repository>>();

				branches.add(new RepositoryTreeNode<Repository>(node,
						RepositoryTreeNodeType.BRANCHES, node.getRepository(),
						node.getRepository()));

				branches.add(new RepositoryTreeNode<Repository>(node,
						RepositoryTreeNodeType.PROJECTS, node.getRepository(),
						node.getRepository()));

				return branches.toArray();

			case PROJECTS:

				List<RepositoryTreeNode<File>> projects = new ArrayList<RepositoryTreeNode<File>>();

				// TODO do we want to show the projects here?
				Collection<File> result = new HashSet<File>();
				Set<String> traversed = new HashSet<String>();
				collectProjectFilesFromDirectory(result, repo.getDirectory()
						.getParentFile(), traversed, new NullProgressMonitor());
				for (File file : result) {
					projects.add(new RepositoryTreeNode<File>(node,
							RepositoryTreeNodeType.PROJ, repo, file));
				}

				return projects.toArray();

			default:
				return null;
			}

		}
