#!/usr/bin/java -jar lib/jruby-complete-1.4.0.jar

require 'find'
require 'fileutils'
require 'rbconfig'
if ENV['windir'] == nil
	CLASSPATH_SEPARATOR = ':'
else
	CLASSPATH_SEPARATOR = ';'
end

EXECUTION_OUTPUT_FILES = [ "ascending.txt", "histogram.txt" ]
JUNIT = "lib/junit-4.8.1.jar"
SRC_OUT = "out/src"
TEST_OUT = "out/test"
CLASSPATH = SRC_OUT + CLASSPATH_SEPARATOR + TEST_OUT + CLASSPATH_SEPARATOR + JUNIT
BUILD_ORDER = [[:src, 'SortedValues'], [:test, 'SortedValuesTest'],
               [:src, 'SortedHistogram'], [:test,'SortedHistogramTest'],
               [:src, 'SortedHistogramApplication'], [:test, 'SortedHistogramTestSuite']]

include FileUtils::Verbose

class SortedHistogramBuilder

  def remove_execution_output
    EXECUTION_OUTPUT_FILES.each { |file| rm file if File.exist? file }
  end

  def run_clean
    puts
    puts "Cleaning up old intermediate and output files:"
    puts
    rm_rf 'out'
    remove_execution_output
    puts 
  end

  def run_compile
    puts
    puts "Compiling source files to class files in out directory:"
    puts
    mkpath "out/src"
    mkpath "out/test"
    BUILD_ORDER.each do |file|
      directory = file[0]
      java_source = file[1]
      cmd = "javac #{directory}/com/thegroverfamily/#{java_source}.java -d out/#{directory} " +
            "-classpath #{CLASSPATH}"
      puts cmd
      `#{cmd}`
    end
  end

  def run_unit_test
    puts
    puts "Running JUnit unit tests:"
    puts 
    system "java -cp #{CLASSPATH} org.junit.runner.JUnitCore com.thegroverfamily.SortedHistogramTestSuite"
  end

  def run_classes
    puts
    puts "Running SortedHistogramApplication classes, writing #{EXECUTION_OUTPUT_FILES.join " & "}"
    puts 
    system "java -cp #{CLASSPATH} com.thegroverfamily.SortedHistogramApplication"
  end

MANIFEST_CONTENTS =<<EOD
Manifest-Version: 1.0
Main-Class: com.thegroverfamily.SortedHistogramApplication
EOD

  def run_jar
    puts
    puts "Running sorted-histogram.jar (after making MANIFEST.MF file and .jar file itself):"
    puts 
    File.open("out/MANIFEST.MF", "w+") do |file|
      file.puts MANIFEST_CONTENTS
    end        
    cd "out/src" do
      system "jar cvfm ../sorted-histogram.jar ../MANIFEST.MF *"
    end
    puts
    puts "Acceptance testing results of running executable out/sorted-histogram.jar:"
    puts     
    remove_execution_output
    system "java -jar out/sorted-histogram.jar"
    run_acceptance_test
     
  end

  def run_acceptance_test
    puts
    puts "Comparing files with expected output as an acceptance test:"
    puts

    total_values = 0
    last_value = -1
    File.open EXECUTION_OUTPUT_FILES[0] do |file|
      file.each_line do |line|
        puts "    #{line}"
        value = line.to_i
        if (value >= last_value)
          last_value = value
          total_values += 1
        else
          puts "  Sorted values test FAILED! (values were not in order)"
          exit
        end
      end
      puts "  Sorted values test passed (#{total_values} values)."
    end

    puts

    File.open EXECUTION_OUTPUT_FILES[1] do |file|
      total_lines = 0
      last_value = -1
      occurrences = 0
      file.each_line do |line|
        numbers = line.split ' - '
        puts "    #{numbers[0]} - #{numbers[1]}"
        total_lines += 1
        occurrences += numbers[1].to_i
        if last_value > numbers[0].to_i
          puts "  Histogram test FAILED! (values were not in order)"
          exit
        end
      end
      if occurrences == total_values
        puts "  Histogram test passed (#{total_lines} lines, #{occurrences} values)."      
      else
        puts "  Histogram test FAILED! (total values were #{occurrences}, not #{total_values})"
      end
    end

    puts

  end

  def run_all
    run_clean
    run_compile
    run_unit_test
    run_classes
    run_acceptance_test
    run_jar
  end
  
  def runner_methods
    runners = methods.inject([]) do |runners, method|
      runners.push method.sub(/run_/, '') if method =~ /^run_/
      runners
    end
    return runners.sort!
  end
  
end



if __FILE__ == $0

  builder = SortedHistogramBuilder.new

  if ARGV.length > 0
    if builder.runner_methods.include? ARGV[0]
      eval "builder.run_#{ARGV[0]}"
    else
      puts
      puts "USAGE:  run [command]"
      puts "  Commands are: #{builder.runner_methods.join ', '}"
      puts "  (omission of the command argument runs steps above in correct build order)"
      puts
    end 
  else
    puts
    puts "Running all steps, to run individual steps add a command-line argument:"
    puts
    builder.run_all
  end
  
end

