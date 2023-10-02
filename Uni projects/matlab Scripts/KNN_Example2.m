% This example do the followings: 
% - Read fisheriris dataset that contains two components (1- meas (i.e., data points, 2- species (i.e., label of each data point) 
% - Divide the dataset randomly into training and testing examples.
% - Train and test the K-NN
% - Create the Confusion matrix to describe the performance of the K-NN classification model 
% - Calculate the Average Correct Classification Performance
clear;close all; clc;
load fisheriris.mat;

dataset=meas;

% add label to your dataset
  for i=1:size(dataset,1)
      if i<=50
          Class_Lab(i)=1;
      elseif (i>50) && (i<=100)
           Class_Lab(i)=2;
      elseif (i>100)
          Class_Lab(i)=3;
       
      end         
  end

  Class_Lab=Class_Lab'; % This vector contains the label for the dataset
   dataset(:,5)=Class_Lab;  % Add the label to the original dataset 

   % divide the dataset into training and testing
     Total_Samples = size(dataset,1);
     Selected_Samples = Total_Samples*0.6; % 60% of the total samples
     assert(Selected_Samples<=Total_Samples); %  cannot choose more rows than exist in the matrix');
     rand_rows = randperm(Total_Samples);
     
  k=1;  
j=1;
      for i=1:size(dataset,1)
         if k<=Selected_Samples 
        TR_Temp{i}=dataset(rand_rows(i),:); 
        k=k+1;
         else 
             TE_Temp{j}=dataset(rand_rows(i),:);   
          k=k+1;
          j=j+1;
         end

      end
  Training_Dataset=cell2mat(TR_Temp');
  Testing_Dataset=cell2mat(TE_Temp');   
%   
%    Training_Dataset2=Training_Dataset(:,1:4);
   Testing_Dataset2=Testing_Dataset(:,1:4); 
%    Class_Lab_Training=Training_Dataset(:,5);
   Class_Lab_Testing=Testing_Dataset(:,5);

   
 for m=1:2
 
 K=5+2*(m-1);
 Mdl = fitcknn(Training_Dataset(:,1:4),Training_Dataset(:,5),'NumNeighbors',K);
 
 for i=1:size(Testing_Dataset2,1)
   Testing_Examples = Testing_Dataset2(i,:); % 
   Pred_KNN(i) = predict(Mdl,Testing_Examples);
 end
 
 % Confusion matrix for testing data
 for i=1:3   % 3 is the number of classes
  in1=find(Class_Lab_Testing==i);  % for example if i=1, in1 would contain the index of all data points that classified as Class 1
  nor=length(in1);                 % NOR is the number of data points that classified as class 1 
  
  for j=1:3
    Classification=length(find(Pred_KNN(in1)==j)); % Correct classification for each class 
    Confusion_Matrix(j,i)=Classification/nor*100;   % calculate the percentage 
  end
 end
 
   display (K, 'K=')
   display (Confusion_Matrix,'KNN:confusion matrix for testing data');

% % %of correct classifications for testing data
  Average_Correct_Classification=length(find((Pred_KNN-Class_Lab_Testing')==0))/length(Class_Lab_Testing)*100;
 display (K, 'K=')
 display (Average_Correct_Classification, 'KNN: Percentage of correct classifications for testing data')
 end



 