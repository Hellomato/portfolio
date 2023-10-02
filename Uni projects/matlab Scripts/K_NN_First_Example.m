% % This example do the followings: 
% - Create a dataset in the range of 10-20 for X coordinate and between 25 to 50 for Y coordinate 
% - Label the dataset into two classes (1 and 2).
% - Divide the dataset randomly into training and testing examples.
% - Train and test the K-NN
% - Plot data points based upon the corresponding class label for each data point as well one testing data point.  
clear;close all; clc;
rng(0);
xmin=10;
xmax=20;
n=200;
x=[xmin+rand(1,n)*(xmax-xmin)];
x=x';

xmin=25;
xmax=50;
y=[xmin+rand(1,n)*(xmax-xmin)];
y=y';

dataset=[x y];

 plot(dataset(:,1),dataset(:,2),'ro')
 title 'Randomly Generated Data'
 for i=1:size(dataset,1)
     if i<=100
         Class_Lab(i)=1;
     elseif (i>100) 
         Class_Lab(i)=2;
     
     end
         
 end

  Class_Lab=Class_Lab';
  dataset(:,3)=Class_Lab;
  
     Total_Samples = size(dataset,1);
     Selected_Samples = Total_Samples*0.6; % 60% of the total samples
     assert(Selected_Samples<=Total_Samples); %  cannot choose more rows than exist in the matrix');
     rand_rows = randperm(Total_Samples);
     
     
  k=1;  
j=1;
      for i=1:size(dataset,1)
         if k<=Selected_Samples   % if k <=120 assign random rows to the training dataset
        TR_Temp{i}=dataset(rand_rows(i),:); 
        k=k+1;
         else   % if k >120 assign random rows to the testing dataset (by the way, data that is assigned to the training dataset would not appear in the testing dataset
             TE_Temp{j}=dataset(rand_rows(i),:);   
          k=k+1;
          j=j+1;
         end

      end
  Training_Dataset=cell2mat(TR_Temp');
  Testing_Dataset=cell2mat(TE_Temp');   
  
  Training_Dataset2=Training_Dataset(:,1:2);
  Testing_Dataset2=Testing_Dataset(:,1:2); 
  Class_Lab_Training=Training_Dataset(:,3);

   k=3;

  Mdl = fitcknn(Training_Dataset2,Class_Lab_Training,'NumNeighbors',k)
   Classification=predict(Mdl, Testing_Dataset2(19,:)) 
figure; 
hold on
% 
 plot(dataset(Class_Lab==1,1),dataset(Class_Lab==1,2),'b.', 'MarkerSize',12)
 plot(dataset(Class_Lab==2,1),dataset(Class_Lab==2,2),'g.', 'MarkerSize',12)
 plot(Testing_Dataset2(19,1),Testing_Dataset2(19,2),'kX', 'MarkerSize',20)

