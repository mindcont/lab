'''
Coding Just for Fun
Created by burness on 16/9/8.
'''


def compute_score(true_dat_file,our_result_file):
    true_dict = {}
    with open(true_dat_file,'r') as fread:
        i=0
        for line in fread.readlines():
            i+=1
            line_list = line.split("\t")
            file_name = line_list[0]
            file_label = line_list[1]
            #print file_name,file_label
            file_is_hidden = line_list[3].rstrip('\n')
            #print file_is_hidden

            if '0' in file_is_hidden:
                #print '1'
                true_dict[file_name] = file_label


    top1_correct = 0
    top2_correct = 0
    #print true_dict
    with open(our_result_file, 'r') as fread:
        for line in fread.readlines():
            line_list = line.split()
            file_name = line_list[0]
            #print file_name
            #print line_list[3]
            try:
                #print line_list[1], true_dict[file_name]
                if line_list[1] == true_dict[file_name]:
                    top1_correct += 1
                if line_list[3] == true_dict[file_name]:
                    top2_correct += 1
            except:
                continue

    print i
    top1_ratio = 1.0*top1_correct/i
    top2_ratio = 1.0*top2_correct/i

    print "the top1: {0} and top2: {1} ".format(top1_ratio,top2_ratio)
    print "the sum score: {0}".format(top1_ratio+0.4*top2_ratio)


if __name__ == '__main__':
    compute_score('../results/20160924/BOT_Image_Testset_6.txt','../results/20160924/ADTC_DLU_results_6.txt')
