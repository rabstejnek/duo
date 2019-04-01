-- vim: ts=2 sw=2 sts=2 expandtab:cindent:formatoptions+=cro  
--------- --------- --------- --------- --------- ---------   

require "lib"
require "rows"

function dom(t,row1,row2,     n,a0,a,b0,b,s1,s2)
  s1,s2,n = 0,0, 0 --NOTE: w is -1 for <, 1 for >. AKA it denotes whether we prioritize high or low values.
  for _ in pairs(t.w) do n=n+1 end --Set n to the number of relevant columns.
  for c,w in pairs(t.w) do --For all relevant columns (those with < or >)...
    a0 = row1[c] --Grab the value from row 1
    b0 = row2[c] --Grab the value from row 2
    a  = numNorm( t.nums[c], a0) --"To gauge the overall signal, we average across all goals"
    b  = numNorm( t.nums[c], b0) --"To gauge the overall signal, we average across all goals"
    s1 = s1 - 10^(w * (a-b)/n) --"To make the signal stronger, we shout the difference (raise it to some expendial power)."
    s2 = s2 - 10^(w * (b-a)/n) --"To make the signal stronger, we shout the difference (raise it to some expendial power)."
  end
  return s1/n < s2/n --We divide by n since we are doing the calculation n times above.
end

function doms(t,  n,c,row1,row2,s)
  n= Lean.dom.samples --Find the number of samples
  c= #t.name + 1 --Column number for dom is # of columns + 1
  print(cat(t.name,",") .. ",>dom") --Put ">dom" as header of that column
  for r1=1,#t.rows do --For every row...
    row1 = t.rows[r1] --Define variable for that row
    row1[c] = 0 --Initialize the dom column as 0
    for s=1,n do --Over the sample size...
     row2 = another(r1,t.rows)  --Get a random, different row
     s = dom(t,row1,row2) and 1/n or 0 --Compute dom on this row.
     row1[c] = row1[c] + s end end --If dom was true add 1/n, otherwise add 0.
                                   --This is because we are doing an average, and each calculation will only count 1/n amount.
  dump(t.rows) --Send rows to std out
end

function mainDom() doms(rows()) end

return {main = mainDom}
