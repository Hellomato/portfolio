clear all;
close all;
rng shuffle;
Y = gen_superdata(10554466);

rows = size(Y,1);
fprintf('rows');
disp(rows);

columns = size(Y,2);

for c = 1:columns-1
    temp = Y(:,c);
    M = mean(temp);
    fprintf('Mean of column %d is %f\n',c,M);
    sd = std(temp);
    fprintf('standard dev of column %d is %f\n',c,sd);
end
NoC = max(Y(:,6));
fprintf('Number of classes is %i\n',NoC);

covarianceMatrix = cov(Y);
fprintf('Covariance matrix is \n')
disp(covarianceMatrix);
correlationMatrix = corrcov(covarianceMatrix);
fprintf('Correlation matrix is \n')
disp(correlationMatrix);

%get 60% value
ltr = round(rows*0.6);
%get 40% value
lte = rows - ltr;
%make sure there is less training then total
assert(ltr < rows);
%randomly sort array
rRows = randperm(rows);
%preallocate for efficiency
TrainingSet = zeros(ltr,columns);
TestingSet = zeros(lte,columns);
%fill training set with first 60
for i=1:rows
    if i <= ltr 
        TrainingSet(i,:) = Y(rRows(i),:);
    else
        TestingSet((i-ltr),:)= Y(rRows(i),:);
    end

end



k=5;

for a=1:2
    
    fitknn = fitcknn(TrainingSet(:,1:5),TrainingSet(:,6),'NumNeighbors',k);
    for i=1:size(TestingSet(:,1:5),1)
        Pred_KNN(i) = predict(fitknn,TestingSet(i,1:5));
    end
    
    %confusion matrix
    for i=1:NoC %max of this is number of classes
        in1=find(TestingSet(:,6)==i);
        nor=length(in1); %number of datas classified as in1
        for j=1:NoC
            Classification=length(find(Pred_KNN(in1)==j));
            Con_Matrix(j,i)=Classification/nor*100;
         end
    end

    
    %percentage correct
    percentCorrect = length(find((Pred_KNN-TestingSet(:,6)')==0))/length(TestingSet(:,5))*100;
    
    fprintf('For k = %d \n',k);
    fprintf('Confusion Matrix\n');
    disp(Con_Matrix);
    fprintf('Percentage Correct\n');
    disp(percentCorrect)

    k= k + 2;


end


