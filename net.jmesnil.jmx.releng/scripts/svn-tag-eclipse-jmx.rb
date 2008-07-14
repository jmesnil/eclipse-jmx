require "fileutils"

# tag argument is mandatory.
# it is expected to follow the format IYYYYMMDDhhmm (e.g. I200702071405)
tag = ARGV[0]
unless tag =~ /I\d{12}/
  puts "#{tag} is not a valid integration task (must be of the form IYYYYMMDDhhmm)"
  exit 1
end

projects = %w{
  net.jmesnil.jmx.core
  net.jmesnil.jmx.feature
  net.jmesnil.jmx.resources
  net.jmesnil.jmx.resources.test
  net.jmesnil.jmx.ui
  net.jmesnil.jmx.ui.test
  net.jmesnil.jmx.ui.test.interactive}

repository = "https://eclipse-jmx.googlecode.com/svn"
tag_url = "#{repository}/tags/#{tag}"

username = "jmesnil"

mkdir_tag_cmd = "svn mkdir --username=#{username} -m '* creating tag #{tag}' #{tag_url}"
puts mkdir_tag_cmd
system mkdir_tag_cmd

workspace_dir = File.dirname(__FILE__) + "/../../"
FileUtils.cd workspace_dir

tag_msg = "'* tagged as #{tag}'"
projects.each do | project |
  tag_cmd = "svn copy -m #{tag_msg} #{project} #{tag_url}/#{project}"
  puts tag_cmd
  system tag_cmd
end
