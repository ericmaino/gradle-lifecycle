package net.meeteric.gradle.plugins

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.logging.Logger
import org.slf4j.LoggerFactory

abstract class CascadingTasksPluginBase implements Plugin<Project> {

    private static final String LOCKING_GROUP = 'Locking'
    private static final String VERIFICATION_GROUP = 'verification'
    private static final String BUILD_GROUP = 'build'
    private static final String PUBLISHING_GROUP = 'publishing'

    TaskProvider<Task> findOrCreateTask(TaskContainer tasks, String taskName, String taskGroup, String taskDescription) {
        TaskProvider<Task> result = null
        Task task = tasks.findByPath(taskName)

        if (task == null) {
            result = tasks.register(taskName)
            result.configure {
                group = taskGroup
                description = taskDescription
            }
        } else {
            result = tasks.named(task.name)
        }

        return result
    }

    abstract void registerTask(String taskName, String taskGroup, String taskDescription, Project currentProject)

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            project.logger.quiet("Applying Cascading Plugin - ${project.name}")
            registerTask('build', BUILD_GROUP, 'Assembles and tests all composite projects', project)
            registerTask('clean', BUILD_GROUP, 'Deletes to build directory for all composite projects', project)
            registerTask('test', VERIFICATION_GROUP, 'Runs tests for all composite projects', project)
            registerTask('publish', PUBLISHING_GROUP, 'Publishes all composite projects', project)
            registerTask('publishToMavenLocal', PUBLISHING_GROUP, 'Publishes all composite projects to maven local', project)
            registerTask('assemble', BUILD_GROUP, 'Assembles the output of all composite projects', project)
            registerTask('generateLock', LOCKING_GROUP, 'Create a new lock files in build/<configured name>',  project)
            registerTask('saveLock', LOCKING_GROUP, 'Moves the generated lock into the active lock', project)
        }
    }

}
